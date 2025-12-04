package com.shop.service.impl;

import com.shop.dto.CartAddDTO;
import com.shop.entity.Product;
import com.shop.service.CartService;
import com.shop.service.ProductService;
import com.shop.vo.CartItemVO;
import com.shop.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProductService productService;

    private static final String CART_PREFIX = "cart:";

    @Override
    public void add(String username, CartAddDTO cartAddDTO) {
        String key = CART_PREFIX + username;
        String productId = cartAddDTO.getProductId().toString();
        Integer quantity = cartAddDTO.getQuantity();

        // Check if item exists
        Object existingQty = redisTemplate.opsForHash().get(key, productId);
        if (existingQty != null) {
            int currentQty = Integer.parseInt(existingQty.toString());
            redisTemplate.opsForHash().put(key, productId, String.valueOf(currentQty + quantity));
        } else {
            redisTemplate.opsForHash().put(key, productId, String.valueOf(quantity));
        }
    }

    @Override
    public CartVO list(String username) {
        String key = CART_PREFIX + username;
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

        CartVO cartVO = new CartVO();
        List<CartItemVO> items = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        int totalQuantity = 0;

        if (entries.isEmpty()) {
            cartVO.setItems(items);
            cartVO.setTotalPrice(totalPrice);
            cartVO.setTotalQuantity(totalQuantity);
            return cartVO;
        }

        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            Long productId = Long.valueOf(entry.getKey().toString());
            Integer quantity = Integer.valueOf(entry.getValue().toString());

            Product product = productService.getById(productId);
            if (product != null) {
                CartItemVO item = new CartItemVO();
                item.setProductId(productId);
                item.setProductName(product.getName());
                item.setProductIcon(product.getImage()); // Assuming 'image' field exists
                item.setPrice(product.getPrice());
                item.setQuantity(quantity);
                BigDecimal subTotal = product.getPrice().multiply(new BigDecimal(quantity));
                item.setSubTotal(subTotal);

                items.add(item);
                totalPrice = totalPrice.add(subTotal);
                totalQuantity += quantity;
            } else {
                // Product might be deleted, remove from cart
                redisTemplate.opsForHash().delete(key, productId.toString());
            }
        }

        cartVO.setItems(items);
        cartVO.setTotalPrice(totalPrice);
        cartVO.setTotalQuantity(totalQuantity);
        return cartVO;
    }

    @Override
    public void update(String username, Long productId, Integer quantity) {
        String key = CART_PREFIX + username;
        if (quantity > 0) {
            redisTemplate.opsForHash().put(key, productId.toString(), quantity.toString());
        } else {
            delete(username, productId);
        }
    }

    @Override
    public void delete(String username, Long productId) {
        String key = CART_PREFIX + username;
        redisTemplate.opsForHash().delete(key, productId.toString());
    }
}
