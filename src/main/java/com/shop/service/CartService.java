package com.shop.service;

import com.shop.dto.CartAddDTO;
import com.shop.vo.CartVO;

/**
 * 购物车服务接口
 * 定义购物车相关的业务逻辑方法
 */
public interface CartService {
    /**
     * 添加商品到购物车
     * @param username 用户名
     * @param cartAddDTO 购物车添加信息，包含商品SKU ID和数量
     */
    void add(String username, CartAddDTO cartAddDTO);
    
    /**
     * 获取用户购物车列表
     * @param username 用户名
     * @return 购物车信息，包含商品列表和总金额
     */
    CartVO list(String username);
    
    /**
     * 更新购物车商品数量
     * @param username 用户名
     * @param productId 商品ID
     * @param quantity 新的数量
     */
    void update(String username, Long productId, Integer quantity);
    
    /**
     * 从购物车删除商品
     * @param username 用户名
     * @param productId 商品ID
     */
    void delete(String username, Long productId);
}