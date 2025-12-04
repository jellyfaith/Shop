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
 * 商城用户接口
 */
@RestController
@RequestMapping("/shop/user")
@Tag(name = "商城-用户接口")
public class ShopUserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 登录结果
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<String> login(@RequestBody UserLoginDTO loginDTO) {
        return userService.login(loginDTO);
    }

    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     * @return 注册结果
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<String> register(@RequestBody UserRegisterDTO registerDTO) {
        return userService.register(registerDTO);
    }

    /**
     * 获取当前用户信息
     *
     * @param request HTTP请求
     * @return 用户信息
     */
    @GetMapping("/info")
    @Operation(summary = "获取当前用户信息")
    public Result<User> info(HttpServletRequest request) {
        // Assuming JwtInterceptor puts "username" or "userId" in request attribute
        // For now, we might need to fetch by username from service
        // This is a placeholder implementation
        return Result.success(); 
    }
}
