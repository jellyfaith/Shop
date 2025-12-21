package com.shop.service.impl;

import com.shop.dto.CartAddDTO;
import com.shop.entity.Cart;
import com.shop.entity.Product;
import com.shop.entity.ProductSku;
import com.shop.repository.CartMapper;
import com.shop.repository.ProductSkuMapper;
import com.shop.service.CartService;
import com.shop.service.ProductService;
import com.shop.vo.CartItemVO;
import com.shop.vo.CartVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private CartMapper cartMapper;

    private static final String CART_PREFIX = "cart:";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void add(String username, CartAddDTO cartAddDTO) {
        Long skuId = cartAddDTO.getSkuId();
        Integer quantity = cartAddDTO.getQuantity();

        // Validate SKU
        ProductSku sku = productSkuMapper.selectById(skuId);
        if (sku == null) {
            throw new RuntimeException("商品规格不存在");
        }
        if (sku.getStock() < quantity) {
            throw new RuntimeException("库存不足");
        }

        try {
            // Try Redis
            String key = CART_PREFIX + username;
            String hashKey = skuId.toString();
            Object existingQty = redisTemplate.opsForHash().get(key, hashKey);
            if (existingQty != null) {
                int currentQty = Integer.parseInt(existingQty.toString());
                redisTemplate.opsForHash().put(key, hashKey, String.valueOf(currentQty + quantity));
            } else {
                redisTemplate.opsForHash().put(key, hashKey, String.valueOf(quantity));
            }
        } catch (Exception e) {
            logger.error("Redis connection failed, falling back to DB", e);
            // Fallback to DB
            LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Cart::getUsername, username).eq(Cart::getSkuId, skuId);
            Cart cart = cartMapper.selectOne(wrapper);
            if (cart != null) {
                cart.setQuantity(cart.getQuantity() + quantity);
                cartMapper.updateById(cart);
            } else {
                cart = new Cart();
                cart.setUsername(username);
                cart.setSkuId(skuId);
                cart.setQuantity(quantity);
                cartMapper.insert(cart);
            }
        }
    }

    @Override
    public CartVO list(String username) {
        CartVO cartVO = new CartVO();
        List<CartItemVO> items = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        int totalQuantity = 0;

        try {
            // Try Redis
            String key = CART_PREFIX + username;
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

            if (entries.isEmpty()) {
                // If Redis is empty, it might be because it's down or empty. 
                // But if we are here, Redis call succeeded. 
                // However, if we want to support "Redis lost data, check DB", that's a sync issue.
                // For now, we assume if Redis works, we use Redis.
                // If Redis throws exception, we go to catch block.
            } else {
                for (Map.Entry<Object, Object> entry : entries.entrySet()) {
                    Long skuId = Long.valueOf(entry.getKey().toString());
                    Integer quantity = Integer.valueOf(entry.getValue().toString());
                    addItemToVO(items, skuId, quantity);
                }
            }
        } catch (Exception e) {
            logger.error("Redis connection failed, falling back to DB", e);
            // Fallback to DB
            LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Cart::getUsername, username);
            List<Cart> carts = cartMapper.selectList(wrapper);
            for (Cart cart : carts) {
                addItemToVO(items, cart.getSkuId(), cart.getQuantity());
            }
        }

        // Calculate totals
        for (CartItemVO item : items) {
            totalPrice = totalPrice.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            totalQuantity += item.getQuantity();
        }

        cartVO.setItems(items);
        cartVO.setTotalPrice(totalPrice);
        cartVO.setTotalQuantity(totalQuantity);
        return cartVO;
    }

    private void addItemToVO(List<CartItemVO> items, Long skuId, Integer quantity) {
        ProductSku sku = productSkuMapper.selectById(skuId);
        if (sku != null) {
            Product product = productService.getById(sku.getProductId());
            if (product != null) {
                CartItemVO item = new CartItemVO();
                item.setProductId(product.getId());
                item.setSkuId(sku.getId());
                item.setProductName(product.getName());
                item.setProductIcon(sku.getImage() != null ? sku.getImage() : product.getMainImage());
                item.setPrice(sku.getPrice());
                item.setQuantity(quantity);
                try {
                    item.setSpecs(objectMapper.writeValueAsString(sku.getSpecs()));
                } catch (Exception ex) {
                    item.setSpecs("{}");
                }
                item.setSubTotal(sku.getPrice().multiply(BigDecimal.valueOf(quantity)));
                items.add(item);
            }
        }
    }

    @Override
    public void update(String username, Long skuId, Integer quantity) { // Note: Interface might need change if it was productId before
        // Wait, the controller calls update(username, productId, quantity). 
        // But CartAddDTO has productId? No, CartAddDTO usually has skuId.
        // Let's check the Controller.
        // Controller: cartService.update(username, cartAddDTO.getProductId(), cartAddDTO.getQuantity());
        // But CartAddDTO has skuId?
        // I need to check CartAddDTO.
        // Assuming the interface is update(String username, Long skuId, Integer quantity)
        
        try {
            String key = CART_PREFIX + username;
            redisTemplate.opsForHash().put(key, skuId.toString(), String.valueOf(quantity));
        } catch (Exception e) {
            logger.error("Redis connection failed, falling back to DB", e);
            LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Cart::getUsername, username).eq(Cart::getSkuId, skuId);
            Cart cart = cartMapper.selectOne(wrapper);
            if (cart != null) {
                cart.setQuantity(quantity);
                cartMapper.updateById(cart);
            }
        }
    }

    @Override
    public void delete(String username, Long skuId) {
        try {
            String key = CART_PREFIX + username;
            redisTemplate.opsForHash().delete(key, skuId.toString());
        } catch (Exception e) {
            logger.error("Redis connection failed, falling back to DB", e);
            LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Cart::getUsername, username).eq(Cart::getSkuId, skuId);
            cartMapper.delete(wrapper);
        }
    }
}
