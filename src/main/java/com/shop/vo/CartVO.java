package com.shop.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车视图对象
 * 用于在前端展示完整的购物车信息，包括购物车中的商品列表、总价和总数量
 */
@Data // 使用Lombok的@Data注解，自动生成getter、setter、equals、hashCode和toString方法
public class CartVO {
    /**
     * 购物车商品项列表
     * 包含购物车中的所有商品项，每个商品项都是CartItemVO类型
     */
    private List<CartItemVO> items;
    
    /**
     * 购物车商品总价
     * 计算方式：所有商品项的小计之和，使用BigDecimal确保精确计算
     */
    private BigDecimal totalPrice;
    
    /**
     * 购物车商品总数量
     * 计算方式：所有商品项的数量之和
     */
    private Integer totalQuantity;
}