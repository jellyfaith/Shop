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

/**
 * 购物车服务实现类
 * 采用Redis作为主要存储，数据库作为备选存储，实现了购物车的增删改查功能
 */
@Service
public class CartServiceImpl implements CartService {

    // 日志记录器
    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    // Redis模板，用于操作Redis缓存
    @Autowired
    private StringRedisTemplate redisTemplate;

    // 商品服务，用于获取商品信息
    @Autowired
    private ProductService productService;

    // 商品SKU数据访问层，用于获取商品规格信息
    @Autowired
    private ProductSkuMapper productSkuMapper;

    // 购物车数据访问层，用于操作数据库中的购物车数据
    @Autowired
    private CartMapper cartMapper;

    // Redis中购物车数据的键前缀
    private static final String CART_PREFIX = "cart:";
    // JSON对象映射器，用于将对象转换为JSON字符串
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 添加商品到购物车
     * @param username 用户名
     * @param cartAddDTO 购物车添加请求数据传输对象，包含商品SKU ID和数量
     */
    @Override
    public void add(String username, CartAddDTO cartAddDTO) {
        Long skuId = cartAddDTO.getSkuId();
        Integer quantity = cartAddDTO.getQuantity();

        // 验证商品SKU是否存在
        ProductSku sku = productSkuMapper.selectById(skuId);
        if (sku == null) {
            throw new RuntimeException("商品规格不存在");
        }
        // 验证库存是否充足
        if (sku.getStock() < quantity) {
            throw new RuntimeException("库存不足");
        }

        try {
            // 优先使用Redis进行操作
            String key = CART_PREFIX + username; // Redis中购物车的键，格式为cart:用户名
            String hashKey = skuId.toString(); // Redis哈希结构中的字段名，使用SKU ID
            Object existingQty = redisTemplate.opsForHash().get(key, hashKey);
            if (existingQty != null) {
                // 如果购物车中已存在该商品，则增加数量
                int currentQty = Integer.parseInt(existingQty.toString());
                redisTemplate.opsForHash().put(key, hashKey, String.valueOf(currentQty + quantity));
            } else {
                // 如果购物车中不存在该商品，则添加新条目
                redisTemplate.opsForHash().put(key, hashKey, String.valueOf(quantity));
            }
        } catch (Exception e) {
            // Redis操作失败时，记录错误日志并回退到数据库操作
            logger.error("Redis连接失败，回退到数据库操作", e);
            // 使用MyBatis-Plus的LambdaQueryWrapper构建查询条件
            LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Cart::getUsername, username).eq(Cart::getSkuId, skuId);
            Cart cart = cartMapper.selectOne(wrapper);
            if (cart != null) {
                // 如果数据库中已存在该购物车条目，则更新数量
                cart.setQuantity(cart.getQuantity() + quantity);
                cartMapper.updateById(cart);
            } else {
                // 如果数据库中不存在该购物车条目，则创建新记录
                cart = new Cart();
                cart.setUsername(username);
                cart.setSkuId(skuId);
                cart.setQuantity(quantity);
                cartMapper.insert(cart);
            }
        }
    }

    /**
     * 获取用户购物车列表
     * @param username 用户名
     * @return 购物车视图对象，包含购物车商品列表、总价格和总数量
     */
    @Override
    public CartVO list(String username) {
        CartVO cartVO = new CartVO();
        List<CartItemVO> items = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO; // 总价格初始化为0
        int totalQuantity = 0; // 总数量初始化为0

        try {
            // 优先从Redis获取购物车数据
            String key = CART_PREFIX + username;
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

            if (!entries.isEmpty()) {
                // 如果Redis中有数据，则遍历处理每个购物车条目
                for (Map.Entry<Object, Object> entry : entries.entrySet()) {
                    Long skuId = Long.valueOf(entry.getKey().toString());
                    Integer quantity = Integer.valueOf(entry.getValue().toString());
                    // 将购物车条目转换为视图对象并添加到列表中
                    addItemToVO(items, skuId, quantity);
                }
            }
            // 如果Redis中没有数据，则直接返回空购物车，不查询数据库
        } catch (Exception e) {
            // Redis操作失败时，记录错误日志并回退到数据库操作
            logger.error("Redis连接失败，回退到数据库操作", e);
            // 查询数据库中的购物车数据
            LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Cart::getUsername, username);
            List<Cart> carts = cartMapper.selectList(wrapper);
            // 遍历数据库中的购物车条目
            for (Cart cart : carts) {
                // 将购物车条目转换为视图对象并添加到列表中
                addItemToVO(items, cart.getSkuId(), cart.getQuantity());
            }
        }

        // 计算购物车总价格和总数量
        for (CartItemVO item : items) {
            // 计算每个商品的小计并累加到总价格
            totalPrice = totalPrice.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            // 累加商品数量到总数量
            totalQuantity += item.getQuantity();
        }

        // 设置购物车视图对象的属性
        cartVO.setItems(items);
        cartVO.setTotalPrice(totalPrice);
        cartVO.setTotalQuantity(totalQuantity);
        return cartVO;
    }

    /**
     * 将商品SKU信息转换为购物车条目视图对象
     * @param items 购物车条目视图列表
     * @param skuId 商品SKU ID
     * @param quantity 商品数量
     */
    private void addItemToVO(List<CartItemVO> items, Long skuId, Integer quantity) {
        // 根据SKU ID查询商品规格信息
        ProductSku sku = productSkuMapper.selectById(skuId);
        if (sku != null) {
            // 根据商品ID查询商品信息
            Product product = productService.getById(sku.getProductId());
            if (product != null) {
                // 创建购物车条目视图对象
                CartItemVO item = new CartItemVO();
                item.setProductId(product.getId());
                item.setSkuId(sku.getId());
                item.setProductName(product.getName());
                // 设置商品图标，优先使用SKU图片，没有则使用商品主图
                item.setProductIcon(sku.getImage() != null ? sku.getImage() : product.getMainImage());
                item.setPrice(sku.getPrice());
                item.setQuantity(quantity);
                try {
                    // 将商品规格转换为JSON字符串
                    item.setSpecs(objectMapper.writeValueAsString(sku.getSpecs()));
                } catch (Exception ex) {
                    // 转换失败时使用空JSON对象
                    item.setSpecs("{}");
                }
                // 计算商品小计
                item.setSubTotal(sku.getPrice().multiply(BigDecimal.valueOf(quantity)));
                // 将购物车条目添加到列表中
                items.add(item);
            }
        }
    }

    /**
     * 更新购物车商品数量
     * @param username 用户名
     * @param skuId 商品SKU ID
     * @param quantity 新的商品数量
     */
    @Override
    public void update(String username, Long skuId, Integer quantity) {
        try {
            // 优先使用Redis更新购物车商品数量
            String key = CART_PREFIX + username;
            redisTemplate.opsForHash().put(key, skuId.toString(), String.valueOf(quantity));
        } catch (Exception e) {
            // Redis操作失败时，记录错误日志并回退到数据库操作
            logger.error("Redis连接失败，回退到数据库操作", e);
            // 查询数据库中的购物车条目
            LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Cart::getUsername, username).eq(Cart::getSkuId, skuId);
            Cart cart = cartMapper.selectOne(wrapper);
            if (cart != null) {
                // 更新购物车商品数量
                cart.setQuantity(quantity);
                cartMapper.updateById(cart);
            }
        }
    }

    /**
     * 删除购物车商品
     * @param username 用户名
     * @param skuId 商品SKU ID
     */
    @Override
    public void delete(String username, Long skuId) {
        try {
            // 优先使用Redis删除购物车商品
            String key = CART_PREFIX + username;
            redisTemplate.opsForHash().delete(key, skuId.toString());
        } catch (Exception e) {
            // Redis操作失败时，记录错误日志并回退到数据库操作
            logger.error("Redis连接失败，回退到数据库操作", e);
            // 删除数据库中的购物车条目
            LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Cart::getUsername, username).eq(Cart::getSkuId, skuId);
            cartMapper.delete(wrapper);
        }
    }
}