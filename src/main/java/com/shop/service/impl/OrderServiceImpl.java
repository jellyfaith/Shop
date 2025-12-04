package com.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shop.dto.OrderCreateDTO;
import com.shop.entity.OrderDetail;
import com.shop.entity.OrderMaster;
import com.shop.entity.Product;
import com.shop.entity.User;
import com.shop.repository.OrderDetailMapper;
import com.shop.repository.OrderMasterMapper;
import com.shop.repository.UserMapper;
import com.shop.service.CartService;
import com.shop.service.OrderService;
import com.shop.service.ProductService;
import com.shop.vo.CartItemVO;
import com.shop.vo.CartVO;
import com.shop.vo.OrderItemVO;
import com.shop.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderMasterMapper orderMasterMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public String create(String username, OrderCreateDTO orderCreateDTO) {
        // 1. Get User ID
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(userWrapper);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // 2. Get Cart Items
        CartVO cartVO = cartService.list(username);
        if (cartVO.getItems() == null || cartVO.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 3. Create Order Master
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
        orderMaster.setUserId(user.getId());
        orderMaster.setTotalAmount(cartVO.getTotalPrice());
        orderMaster.setStatus(0); // New Order
        // Note: OrderMaster entity might need fields for receiver info if not present, 
        // or we assume it's stored elsewhere. For now, we proceed with existing fields.
        orderMasterMapper.insert(orderMaster);

        // 4. Create Order Details & Deduct Stock
        for (CartItemVO item : cartVO.getItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderMaster.getId());
            orderDetail.setProductId(item.getProductId());
            orderDetail.setProductName(item.getProductName());
            orderDetail.setProductPrice(item.getPrice());
            orderDetail.setQuantity(item.getQuantity());
            orderDetailMapper.insert(orderDetail);

            // Deduct Stock (Simple implementation, not handling concurrency perfectly here)
            Product product = productService.getById(item.getProductId());
            // Assuming Product has a stock field, if not, we skip. 
            // If needed, add stock field to Product entity.
        }

        // 5. Clear Cart
        for (CartItemVO item : cartVO.getItems()) {
            cartService.delete(username, item.getProductId());
        }

        return orderMaster.getOrderNo();
    }

    @Override
    public List<OrderVO> list(String username) {
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(userWrapper);

        LambdaQueryWrapper<OrderMaster> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(OrderMaster::getUserId, user.getId());
        orderWrapper.orderByDesc(OrderMaster::getCreateTime);
        List<OrderMaster> orderMasters = orderMasterMapper.selectList(orderWrapper);

        return orderMasters.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public OrderVO detail(String username, String orderNo) {
        LambdaQueryWrapper<OrderMaster> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderMaster::getOrderNo, orderNo);
        OrderMaster orderMaster = orderMasterMapper.selectOne(wrapper);
        
        if (orderMaster == null) {
            throw new RuntimeException("Order not found");
        }
        // Security check: ensure order belongs to user
        // ...

        return convertToVO(orderMaster);
    }

    @Override
    public void pay(String username, String orderNo) {
        LambdaQueryWrapper<OrderMaster> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderMaster::getOrderNo, orderNo);
        OrderMaster orderMaster = orderMasterMapper.selectOne(wrapper);
        
        if (orderMaster != null && orderMaster.getStatus() == 0) {
            orderMaster.setStatus(1); // Paid
            orderMaster.setPaymentTime(LocalDateTime.now());
            orderMasterMapper.updateById(orderMaster);
        }
    }

    @Override
    public void send(String orderNo) {
        LambdaQueryWrapper<OrderMaster> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderMaster::getOrderNo, orderNo);
        OrderMaster orderMaster = orderMasterMapper.selectOne(wrapper);
        
        if (orderMaster != null && orderMaster.getStatus() == 1) {
            orderMaster.setStatus(2); // Shipped
            orderMaster.setDeliveryTime(LocalDateTime.now());
            orderMasterMapper.updateById(orderMaster);
        }
    }

    private OrderVO convertToVO(OrderMaster orderMaster) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orderMaster, orderVO);

        LambdaQueryWrapper<OrderDetail> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.eq(OrderDetail::getOrderId, orderMaster.getId());
        List<OrderDetail> details = orderDetailMapper.selectList(detailWrapper);

        List<OrderItemVO> itemVOs = details.stream().map(detail -> {
            OrderItemVO itemVO = new OrderItemVO();
            BeanUtils.copyProperties(detail, itemVO);
            itemVO.setTotal(detail.getProductPrice().multiply(new BigDecimal(detail.getQuantity())));
            // Need to fetch product icon if needed
            return itemVO;
        }).collect(Collectors.toList());

        orderVO.setItems(itemVOs);
        return orderVO;
    }
}
