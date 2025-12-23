package com.shop.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单详情实体类
 * 用于存储订单中每个商品项的详细信息
 */
@Data
@TableName("order_detail")  // 指定数据库表名
public class OrderDetail {
    @TableId(type = IdType.AUTO)  // 主键自动增长
    private Long id;  // 订单详情ID

    private Long orderId;  // 订单ID，关联order_master表
    private Long productId;  // 商品ID，关联product表
    private Long skuId;  // SKU ID，关联product_sku表
    private String productName;  // 商品名称（下单时快照）
    private String specs;  // 商品规格（下单时快照）
    private BigDecimal productPrice;  // 商品价格（下单时快照）
    private Integer quantity;  // 购买数量

    @TableField(fill = FieldFill.INSERT)  // 插入时自动填充
    private LocalDateTime createTime;  // 创建时间
}