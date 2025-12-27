package com.shop.security;

import com.shop.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 可选JWT令牌拦截器
 * 用于尝试解析JWT令牌，如果存在且有效则设置用户信息，否则直接放行
 * 适用于公开但需要识别登录用户的接口（如商品详情页记录浏览日志）
 */
@Component
public class OptionalJwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行OPTIONS请求
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String username = jwtUtil.extractUsername(token);
                if (username != null && jwtUtil.validateToken(token, username)) {
                    request.setAttribute("username", username);
                    request.setAttribute("role", jwtUtil.extractRole(token));
                }
            } catch (Exception e) {
                // 忽略令牌解析错误，当作游客处理
            }
        }
        
        // 无论是否解析成功，都放行
        return true;
    }
}
