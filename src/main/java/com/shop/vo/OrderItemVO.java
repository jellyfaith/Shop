package com.shop.vo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 订单商品项视图对象
 * 用于封装订单中单个商品的展示信息，包含商品基本信息、规格、价格和数量等
 * 通常用于订单详情页、订单列表页等需要展示订单商品信息的场景
 */
@Data  // Lombok注解，自动生成getter、setter、toString、equals、hashCode等方法
public class OrderItemVO {
    /**
     * 商品ID
     */
    private Long productId;
    
    /**
     * 商品名称
     */
    private String productName;
    
    /**
     * 商品图片URL
     */
    private String productIcon;
    
    /**
     * 商品规格信息
     * 例如：颜色:红色,内存:8G,存储:128G
     */
    private String specs;
    
    /**
     * 商品单价
     * 使用BigDecimal类型确保金额计算的精度
     */
    private BigDecimal price;
    
    /**
     * 购买数量
     */
    private Integer quantity;
    
    /**
     * 商品总价
     * 计算公式：price * quantity
     * 使用BigDecimal类型确保金额计算的精度
     */
    private BigDecimal total;
}