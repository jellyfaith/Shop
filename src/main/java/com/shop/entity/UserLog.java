package com.shop.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_log")
public class UserLog {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    // VIEW, BUY
    private String actionType;

    // Product ID or Order ID
    private Long targetId;

    // JSON details or simple description
    private String details;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
