package com.shop.security;

import com.shop.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT令牌拦截器
 * 用于拦截请求并验证JWT令牌的有效性，实现基于令牌的身份验证
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    // 注入JWT工具类，用于令牌的解析和验证
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 请求处理前的拦截方法
     * 在请求到达Controller之前执行JWT令牌验证
     *
     * @param request  HTTP请求对象，用于获取请求头中的令牌信息
     * @param response HTTP响应对象，用于设置响应状态码
     * @param handler  处理器对象，即要执行的Controller方法
     * @return boolean 如果令牌验证通过返回true，否则返回false
     * @throws Exception 处理过程中可能抛出的异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行OPTIONS请求（预检请求），这是跨域请求中的常见处理方式
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        // 从请求头中获取Authorization字段，JWT令牌通常放在这个字段中
        String authHeader = request.getHeader("Authorization");
        
        // 检查Authorization头是否存在且以"Bearer "开头（JWT令牌的标准格式）
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // 提取令牌部分（去掉"Bearer "前缀）
            String token = authHeader.substring(7);
            
            try {
                // 从令牌中提取用户名
                String username = jwtUtil.extractUsername(token);
                
                // 验证令牌的有效性（包括过期时间等）
                if (username != null && jwtUtil.validateToken(token, username)) {
                    // 令牌验证通过，将用户名和角色信息存入请求属性中，方便后续Controller使用
                    request.setAttribute("username", username);
                    request.setAttribute("role", jwtUtil.extractRole(token));
                    // 放行请求
                    return true;
                }
            } catch (Exception e) {
                // 令牌无效或解析失败时捕获异常，继续执行后续代码返回401
                // 实际项目中可以在这里记录日志
            }
        }
        
        // 令牌验证失败，设置响应状态码为401（未授权）
        response.setStatus(401);
        // 拒绝请求
        return false;
    }
}