package com.shop.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品分类实体类
 * 用于存储商品分类信息
 */
@Data
@TableName("product_category")  // 指定数据库表名
public class ProductCategory {
    @TableId(type = IdType.AUTO)  // 主键自动增长
    private Long id;  // 分类ID

    private String name;  // 分类名称
    private Integer sort;  // 排序权重，值越小越靠前

    @TableField(fill = FieldFill.INSERT)  // 插入时自动填充
    private LocalDateTime createTime;  // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)  // 插入和更新时自动填充
    private LocalDateTime updateTime;  // 更新时间

    @TableLogic  // 逻辑删除标记
    private Integer deleted;  // 删除状态：0-未删除，1-已删除
}