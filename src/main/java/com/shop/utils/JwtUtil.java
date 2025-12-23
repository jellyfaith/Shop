package com.shop.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 用于生成、验证和解析JWT令牌
 */
@Component
public class JwtUtil {

    /**
     * JWT密钥，用于签名和验证令牌
     * 从配置文件中读取
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * JWT过期时间，单位为毫秒
     * 从配置文件中读取
     */
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 获取用于签名JWT的密钥
     * 
     * @return 生成的密钥
     */
    private Key getSigningKey() {
        // 使用密钥字符串生成HMAC SHA密钥
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 生成JWT令牌
     * 
     * @param username 用户名，作为令牌的主题
     * @param role 用户角色，作为令牌的自定义声明
     * @return 生成的JWT令牌字符串
     */
    public String generateToken(String username, String role) {
        // 创建JWT声明
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role); // 添加用户角色声明
        return createToken(claims, username);
    }

    /**
     * 创建JWT令牌的核心方法
     * 
     * @param claims 自定义声明
     * @param subject 令牌主题
     * @return 生成的JWT令牌字符串
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims) // 设置自定义声明
                .setSubject(subject) // 设置主题
                .setIssuedAt(new Date(System.currentTimeMillis())) // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 设置过期时间
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 使用密钥和HS256算法签名
                .compact(); // 压缩生成最终令牌
    }

    /**
     * 验证JWT令牌
     * 
     * @param token JWT令牌
     * @param username 用户名，用于验证令牌归属
     * @return 验证结果，true表示令牌有效，false表示无效
     */
    public Boolean validateToken(String token, String username) {
        // 提取令牌中的用户名
        final String extractedUsername = extractUsername(token);
        // 验证用户名是否匹配且令牌未过期
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    /**
     * 从JWT令牌中提取用户名
     * 
     * @param token JWT令牌
     * @return 提取的用户名
     */
    public String extractUsername(String token) {
        // 提取所有声明并获取主题
        return extractAllClaims(token).getSubject();
    }

    /**
     * 从JWT令牌中提取用户角色
     * 
     * @param token JWT令牌
     * @return 提取的用户角色
     */
    public String extractRole(String token) {
        // 提取所有声明并获取角色声明
        return (String) extractAllClaims(token).get("role");
    }

    /**
     * 从JWT令牌中提取所有声明
     * 
     * @param token JWT令牌
     * @return 令牌中的所有声明
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // 设置用于验证的密钥
                .build()
                .parseClaimsJws(token) // 解析JWT令牌
                .getBody(); // 获取令牌主体（包含所有声明）
    }

    /**
     * 检查JWT令牌是否过期
     * 
     * @param token JWT令牌
     * @return 过期状态，true表示已过期，false表示未过期
     */
    private Boolean isTokenExpired(String token) {
        // 提取过期时间并与当前时间比较
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}