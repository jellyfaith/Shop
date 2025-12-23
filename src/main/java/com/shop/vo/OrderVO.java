package com.shop.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单视图对象
 * 用于封装订单的完整展示信息，包含订单基本信息、收货信息、订单状态和订单商品列表等
 * 通常用于订单详情页、订单列表页等需要展示订单完整信息的场景
 */
@Data  // Lombok注解，自动生成getter、setter、toString、equals、hashCode等方法
public class OrderVO {
    /**
     * 订单编号
     * 唯一标识一个订单的字符串，通常由系统自动生成
     */
    private String orderNo;
    
    /**
     * 订单总金额
     * 订单中所有商品的总价
     * 使用BigDecimal类型确保金额计算的精度
     */
    private BigDecimal totalAmount;
    
    /**
     * 订单状态
     * 0: 新订单（待支付）
     * 1: 已支付（待发货）
     * 2: 已发货（待收货）
     * 3: 已完成（交易结束）
     * 4: 已取消（订单关闭）
     */
    private Integer status;
    
    /**
     * 收货人姓名
     */
    private String receiverName;
    
    /**
     * 收货人电话
     */
    private String receiverPhone;
    
    /**
     * 收货人地址
     */
    private String receiverAddress;
    
    /**
     * 订单创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 订单商品列表
     * 包含订单中所有商品的详细信息
     */
    private List<OrderItemVO> items;
}