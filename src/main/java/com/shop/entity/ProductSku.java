package com.shop.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 商品SKU实体类
 * 用于存储商品的具体规格和库存信息
 */
@Data
@TableName(value = "product_sku", autoResultMap = true)  // 指定数据库表名和自动映射
public class ProductSku {
    @TableId(type = IdType.AUTO)  // 主键自动增长
    private Long id;  // SKU ID

    private Long productId;  // 商品ID，关联product表

    /**
     * 规格参数，例如：{"color": "黑色", "size": "XL"}
     * 数据库中存储为 JSON
     */
    @TableField(typeHandler = JacksonTypeHandler.class)  // 使用Jackson处理JSON类型
    private Map<String, String> specs;  // 规格参数，键值对形式

    private BigDecimal price;  // SKU价格
    private Integer stock;  // 库存数量
    private String image;  // SKU图片URL
    private String skuCode;  // SKU编码

    @TableField(fill = FieldFill.INSERT)  // 插入时自动填充
    private LocalDateTime createTime;  // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)  // 插入和更新时自动填充
    private LocalDateTime updateTime;  // 更新时间

    @TableLogic  // 逻辑删除标记
    private Integer deleted;  // 删除状态：0-未删除，1-已删除
}