package com.shop.vo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 购物车项视图对象
 * 用于在前端展示购物车中的商品信息
 */
@Data // 使用Lombok的@Data注解，自动生成getter、setter、equals、hashCode和toString方法
public class CartItemVO {
    /**
     * 商品ID
     * 标识购物车项对应的商品
     */
    private Long productId;
    
    /**
     * 商品SKU ID
     * 标识购物车项对应的具体商品规格
     */
    private Long skuId;
    
    /**
     * 商品名称
     * 显示在购物车中的商品名称
     */
    private String productName;
    
    /**
     * 商品规格
     * 可以是JSON字符串或格式化后的字符串，用于显示商品的具体规格信息
     */
    private String specs;
    
    /**
     * 商品图标
     * 商品的图片URL，用于在购物车中显示商品图片
     */
    private String productIcon;
    
    /**
     * 商品价格
     * 单个商品的价格，使用BigDecimal确保精确计算
     */
    private BigDecimal price;
    
    /**
     * 商品数量
     * 购物车中该商品的数量
     */
    private Integer quantity;
    
    /**
     * 商品小计
     * 计算方式：价格 * 数量，使用BigDecimal确保精确计算
     */
    private BigDecimal subTotal;
}