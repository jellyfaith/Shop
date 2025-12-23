package com.shop.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单主表实体类
 * 用于存储订单的基本信息和状态
 */
@Data
@TableName("order_master")  // 指定数据库表名
public class OrderMaster {
    @TableId(type = IdType.AUTO)  // 主键自动增长
    private Long id;  // 订单ID

    private String orderNo;  // 订单编号，唯一标识
    private Long userId;  // 用户ID，关联user表
    private BigDecimal totalAmount;  // 订单总金额
    private Integer status;  // 订单状态：0-待付款，1-已付款，2-已发货，3-已完成，4-已取消
    private LocalDateTime paymentTime;  // 支付时间
    private LocalDateTime deliveryTime;  // 发货时间
    private LocalDateTime finishTime;  // 完成时间

    private String receiverName;  // 收货人姓名
    private String receiverPhone;  // 收货人电话
    private String receiverAddress;  // 收货人地址
    private String paymentMethod;  // 支付方式

    @TableField(fill = FieldFill.INSERT)  // 插入时自动填充
    private LocalDateTime createTime;  // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)  // 插入和更新时自动填充
    private LocalDateTime updateTime;  // 更新时间

    @TableLogic  // 逻辑删除标记
    private Integer deleted;  // 删除状态：0-未删除，1-已删除
}