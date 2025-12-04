package com.shop.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.dto.UserLoginDTO;
import com.shop.entity.User;
import com.shop.service.UserService;
import com.shop.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backend/user")
@Tag(name = "后台-用户管理")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Operation(summary = "管理员登录")
    public Result<String> login(@RequestBody UserLoginDTO loginDTO) {
        // In a real app, you might check if the user has 'ADMIN' role here
        return userService.login(loginDTO);
    }

    @GetMapping("/list")
    @Operation(summary = "用户列表")
    public Result<Page<User>> list(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size) {
        // Placeholder for user list
        return Result.success(new Page<>(page, size));
    }
}
