package com.shop.dto;

import lombok.Data;

/**
 * 购物车添加商品数据传输对象
 * 用于接收前端传递的添加商品到购物车的请求参数
 */
@Data
public class CartAddDTO {
    /**
     * 商品ID
     * 对应商品表的主键ID
     */
    private Long productId;
    
    /**
     * 商品SKU ID
     * 对应商品规格表的主键ID，标识具体的商品规格
     */
    private Long skuId;
    
    /**
     * 添加数量
     * 要添加到购物车的商品数量
     */
    private Integer quantity;
}