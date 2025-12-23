package com.shop.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 收货地址实体类
 * 用于存储用户的收货地址信息
 */
@Data
@TableName("receiver")  // 指定数据库表名
public class Receiver {
    @TableId(type = IdType.AUTO)  // 主键自动增长
    private Long id;  // 地址ID

    private Long userId;  // 用户ID，关联user表
    private String name;  // 收货人姓名
    private String phone;  // 收货人电话
    private String address;  // 详细地址
    private Integer isDefault;  // 是否默认地址：0-否，1-是

    @TableField(fill = FieldFill.INSERT)  // 插入时自动填充
    private LocalDateTime createTime;  // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)  // 插入和更新时自动填充
    private LocalDateTime updateTime;  // 更新时间
}