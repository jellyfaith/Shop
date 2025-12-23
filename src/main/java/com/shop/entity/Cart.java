package com.shop.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 购物车实体类
 * 用于存储用户添加到购物车的商品信息
 */
@Data
@TableName("cart_item")  // 指定数据库表名
public class Cart {
    @TableId(type = IdType.AUTO)  // 主键自动增长
    private Long id;  // 购物车项ID

    private String username;  // 用户名，关联user表
    private Long skuId;  // SKU ID，关联product_sku表
    private Integer quantity;  // 购买数量

    @TableField(fill = FieldFill.INSERT)  // 插入时自动填充
    private LocalDateTime createTime;  // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)  // 插入和更新时自动填充
    private LocalDateTime updateTime;  // 更新时间
}