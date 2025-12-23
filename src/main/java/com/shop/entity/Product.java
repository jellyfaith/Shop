package com.shop.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类
 * 用于存储商品的基本信息，是商品的父级概念
 */
@Data
@TableName("product")  // 指定数据库表名
public class Product {
    @TableId(type = IdType.AUTO)  // 主键自动增长
    private Long id;  // 商品ID

    private Long categoryId;  // 商品分类ID，关联product_category表
    private String name;  // 商品名称
    private String description;  // 商品描述
    private String mainImage;  // 商品主图URL
    private BigDecimal minPrice;  // 商品最低价格（不同SKU可能有不同价格）
    private Integer status;  // 商品状态：1-上架，0-下架

    @TableField(fill = FieldFill.INSERT)  // 插入时自动填充
    private LocalDateTime createTime;  // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)  // 插入和更新时自动填充
    private LocalDateTime updateTime;  // 更新时间

    @TableLogic  // 逻辑删除标记
    private Integer deleted;  // 删除状态：0-未删除，1-已删除
}