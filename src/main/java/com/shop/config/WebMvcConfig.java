package com.shop.config;

import com.shop.security.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC配置类
 * 用于配置CORS跨域、拦截器等Spring MVC相关功能
 */
@Configuration // 标记为Spring配置类
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired // 自动注入JWT拦截器
    private JwtInterceptor jwtInterceptor;

    @Autowired
    private com.shop.security.OptionalJwtInterceptor optionalJwtInterceptor;

    /**
     * 配置CORS跨域资源共享
     * 允许前端应用从不同域名访问后端API
     * @param registry CORS注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 对所有路径启用CORS
                .allowedOriginPatterns("*") // 允许所有来源域名
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的HTTP方法
                .allowedHeaders("*") // 允许的请求头
                .allowCredentials(true); // 允许发送Cookie
    }

    /**
     * 配置拦截器
     * 注册JWT拦截器，用于验证请求中的JWT令牌
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor) // 添加JWT拦截器
                .addPathPatterns("/**") // 拦截所有路径
                .excludePathPatterns( // 排除不需要拦截的路径
                        "/user/login", // 用户登录
                        "/user/register", // 用户注册
                        "/shop/user/login", // 商城用户登录
                        "/shop/user/register", // 商城用户注册
                        "/backend/user/login", // 后台管理员登录
                        "/shop/login", // 商城登录
                        "/shop/product/**", // 商城商品相关（公开访问）
                        "/shop/category/**", // 商城分类相关（公开访问）
                        "/category/list", // 分类列表（公开访问）
                        "/swagger-ui/**", // Swagger UI
                        "/v3/api-docs/**", // Swagger API文档
                        "/webjars/**", // Webjars资源
                        "/doc.html" // API文档页面
                );

        // 注册可选JWT拦截器，仅针对商品详情等需要记录日志但公开的接口
        registry.addInterceptor(optionalJwtInterceptor)
                .addPathPatterns("/shop/product/**");
    }
}