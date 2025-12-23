package com.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类
 * 用于配置RedisTemplate，自定义Redis的序列化方式，提高数据存储效率和可读性
 */
@Configuration // 标记为Spring配置类
public class RedisConfig {

    /**
     * 配置RedisTemplate
     * 自定义Key和Value的序列化方式，解决默认序列化方式可读性差的问题
     * @param connectionFactory Redis连接工厂
     * @return RedisTemplate对象，用于Redis操作
     */
    @Bean // 将方法返回的对象注册为Spring Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 设置Redis连接工厂
        template.setConnectionFactory(connectionFactory);
        
        // Key序列化：使用StringRedisSerializer，保持Key的可读性
        template.setKeySerializer(new StringRedisSerializer());
        // Value序列化：使用GenericJackson2JsonRedisSerializer，将对象序列化为JSON格式
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        
        // Hash Key序列化：使用StringRedisSerializer
        template.setHashKeySerializer(new StringRedisSerializer());
        // Hash Value序列化：使用GenericJackson2JsonRedisSerializer
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        
        return template;
    }
}