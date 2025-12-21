package com.shop.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("receiver")
public class Receiver {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private String name;
    private String phone;
    private String address;
    private Integer isDefault; // 0-No, 1-Yes

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
