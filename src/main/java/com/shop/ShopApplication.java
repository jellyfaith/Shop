package com.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 电商系统应用程序主类
 * 这是Spring Boot应用程序的入口点，负责启动整个应用
 */
@SpringBootApplication // 启用Spring Boot自动配置、组件扫描和Spring Boot应用程序注解
public class ShopApplication {

    /**
     * 应用程序的主入口方法
     * 
     * @param args 命令行参数，可以通过命令行传递给应用程序
     */
    public static void main(String[] args) {
        // 启动Spring Boot应用程序，加载所有配置并初始化Spring容器
        SpringApplication.run(ShopApplication.class, args);
    }

}