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

/**
 * 后台用户管理接口
 */
@RestController
@RequestMapping("/backend/user")
@Tag(name = "后台-用户管理")
public class AdminUserController {

    @Autowired
    private UserService userService;

    /**
     * 管理员登录
     *
     * @param loginDTO 登录信息
     * @return 登录结果
     */
    @PostMapping("/login")
    @Operation(summary = "管理员登录")
    public Result<String> login(@RequestBody UserLoginDTO loginDTO) {
        // In a real app, you might check if the user has 'ADMIN' role here
        return userService.login(loginDTO);
    }

    /**
     * 获取用户列表
     *
     * @param page 页码
     * @param size 每页大小
     * @return 用户分页列表
     */
    @GetMapping("/list")
    @Operation(summary = "用户列表")
    public Result<Page<User>> list(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size) {
        // Placeholder for user list
        return Result.success(new Page<>(page, size));
    }
}
