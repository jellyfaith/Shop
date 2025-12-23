package com.shop.controller.shop;

import com.shop.dto.UserLoginDTO;
import com.shop.dto.UserRegisterDTO;
import com.shop.entity.User;
import com.shop.service.UserService;
import com.shop.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商城用户控制器
 * 负责处理商城用户的登录、注册、信息查询和更新等用户管理功能
 */
@RestController
@RequestMapping("/shop/user")
@Tag(name = "商城-用户接口")
public class ShopUserController {

    @Autowired
    private UserService userService;  // 用户服务，处理用户相关业务逻辑

    /**
     * 用户登录
     * @param loginDTO 登录信息，包含用户名和密码
     * @return 登录结果，成功时返回JWT令牌
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<String> login(@RequestBody UserLoginDTO loginDTO) {
        return userService.login(loginDTO);  // 调用服务层处理登录逻辑
    }

    /**
     * 用户注册
     * @param registerDTO 注册信息，包含用户名、密码等
     * @return 注册结果
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<String> register(@RequestBody UserRegisterDTO registerDTO) {
        return userService.register(registerDTO);  // 调用服务层处理注册逻辑
    }

    /**
     * 获取当前用户信息
     * @param request HTTP请求，用于获取当前登录用户的用户名
     * @return 当前登录用户的信息（不包含密码）
     */
    @GetMapping("/info")
    @Operation(summary = "获取当前用户信息")
    public Result<User> info(HttpServletRequest request) {
        // 从请求属性中获取用户名（由JWT拦截器设置）
        String username = (String) request.getAttribute("username");
        if (username == null) {
            return Result.error("未登录");  // 未登录验证
        }
        
        // 根据用户名查询用户信息
        User user = userService.lambdaQuery().eq(User::getUsername, username).one();
        if (user != null) {
            user.setPassword(null); // 不返回密码，保护用户隐私
            return Result.success(user);
        }
        return Result.error("用户不存在");
    }

    /**
     * 更新用户信息
     * @param user 用户信息（不包含密码）
     * @param request HTTP请求，用于获取当前登录用户的用户名
     * @return 更新结果
     */
    @PutMapping("/update")
    @Operation(summary = "更新用户信息")
    public Result<String> update(@RequestBody User user, HttpServletRequest request) {
        // 从请求属性中获取用户名（由JWT拦截器设置）
        String username = (String) request.getAttribute("username");
        if (username == null) {
            return Result.error("未登录");  // 未登录验证
        }
        user.setUsername(username); // 确保更新的是当前登录用户的信息
        return userService.update(user);  // 调用服务层处理更新逻辑
    }
}