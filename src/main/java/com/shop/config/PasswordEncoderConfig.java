package com.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码加密配置类
 * 用于配置密码加密算法，保障用户密码的安全存储
 */
@Configuration // 标记为Spring配置类
public class PasswordEncoderConfig {

    /**
     * 配置BCrypt密码加密器
     * BCrypt是一种安全的密码哈希算法，自动处理盐值和哈希过程
     * @return PasswordEncoder对象，用于密码加密和验证
     */
    @Bean // 将方法返回的对象注册为Spring Bean
    public PasswordEncoder passwordEncoder() {
        // 创建并返回BCrypt密码编码器
        // BCrypt算法会自动生成随机盐值，并将盐值存储在哈希结果中
        return new BCryptPasswordEncoder();
    }
}