package com.shop.dto;

import lombok.Data;

/**
 * 用户登录数据传输对象
 * 用于接收前端传递的用户登录请求参数
 */
@Data
public class UserLoginDTO {
    /**
     * 用户名
     * 用户注册时设置的登录用户名
     */
    private String username;
    
    /**
     * 密码
     * 用户注册时设置的登录密码
     */
    private String password;
}