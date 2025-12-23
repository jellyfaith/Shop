package com.shop.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger API文档配置类
 * 用于生成和配置API文档，方便前后端开发人员查看和测试API接口
 */
@Configuration // 标记为Spring配置类
public class SwaggerConfig {

    /**
     * 配置OpenAPI信息
     * @return OpenAPI对象，包含API文档的基本信息
     */
    @Bean // 将方法返回的对象注册为Spring Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Shop API") // API文档标题
                        .description("Shop Application API Documentation") // API文档描述
                        .version("v1.0.0")); // API文档版本
    }
}