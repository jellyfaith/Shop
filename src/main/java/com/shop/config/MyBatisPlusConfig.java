package com.shop.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus配置类
 * 用于配置MyBatis Plus的全局设置，如分页插件、Mapper扫描等
 */
@Configuration // 标记为Spring配置类
@MapperScan("com.shop.repository") // 扫描Mapper接口所在的包
public class MyBatisPlusConfig {

    /**
     * 配置MyBatis Plus拦截器
     * 用于添加MyBatis Plus的内置插件，如分页插件
     * @return MybatisPlusInterceptor对象，包含所有配置的拦截器
     */
    @Bean // 将方法返回的对象注册为Spring Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件，指定数据库类型为MySQL
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}