package com.shop.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 普通用户实体类
 * 用于存储商城注册用户的基本信息
 */
@Data
@TableName("user")  // 指定数据库表名
public class User {
    @TableId(type = IdType.ASSIGN_ID)  // 主键使用雪花算法生成
    private Long id;  // 用户ID

    private String username;  // 用户名，登录账号
    private String password;  // 密码，加密存储
    private String email;  // 邮箱
    private String phone;  // 手机号
    private String address;  // 收货地址

    @TableField(fill = FieldFill.INSERT)  // 插入时自动填充
    private LocalDateTime createTime;  // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)  // 插入和更新时自动填充
    private LocalDateTime updateTime;  // 更新时间

    @TableLogic  // 逻辑删除标记
    private Integer deleted;  // 删除状态：0-未删除，1-已删除
}