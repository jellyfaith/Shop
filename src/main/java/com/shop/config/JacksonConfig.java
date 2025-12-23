package com.shop.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson JSON序列化配置类
 * 用于自定义Jackson的JSON序列化行为，解决前端处理大数字精度丢失问题
 */
@Configuration // 标记为Spring配置类
public class JacksonConfig {

    /**
     * 配置Jackson序列化器
     * @return Jackson2ObjectMapperBuilderCustomizer对象，用于自定义Jackson配置
     */
    @Bean // 将方法返回的对象注册为Spring Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        // 自定义序列化器，将Long类型转换为String类型
        // 解决前端JavaScript处理大数字(超过2^53)时精度丢失的问题
        return builder -> builder.serializerByType(Long.class, ToStringSerializer.instance);
    }
}