package com.shop.dto;

import lombok.Data;

/**
 * 用户注册数据传输对象
 * 用于接收前端传递的用户注册请求参数
 */
@Data
public class UserRegisterDTO {
    /**
     * 用户名
     * 用户设置的登录用户名，需保证唯一性
     */
    private String username;
    
    /**
     * 密码
     * 用户设置的登录密码，需要进行加密存储
     */
    private String password;
    
    /**
     * 电子邮箱
     * 用户的电子邮箱地址，用于找回密码等功能
     */
    private String email;
    
    /**
     * 手机号码
     * 用户的手机号码，用于接收验证码等功能
     */
    private String phone;
    
    /**
     * 收货地址
     * 用户的默认收货地址
     */
    private String address;
}