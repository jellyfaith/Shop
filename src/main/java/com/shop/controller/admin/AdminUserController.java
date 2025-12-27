package com.shop.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.dto.UserLoginDTO;
import com.shop.entity.User;
import com.shop.service.AdminUserService;
//import com.shop.service.UserService;
import com.shop.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台用户管理控制器
 * 负责处理管理员登录和用户列表查询等后台用户管理功能
 */
@RestController
@RequestMapping("/backend/user")
@Tag(name = "后台-用户管理")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;  // 管理员用户服务，用于处理管理员登录等操作

    @Autowired
    private com.shop.service.UserService userService;

    @Autowired
    private com.shop.service.UserLogService userLogService;

    /**
     * 管理员登录
     * @param loginDTO 登录信息（包含用户名和密码）
     * @return 登录结果（成功返回JWT token，失败返回错误信息）
     */
    @PostMapping("/login")
    @Operation(summary = "管理员登录")
    public Result<String> login(@RequestBody UserLoginDTO loginDTO) {
        return adminUserService.login(loginDTO);
    }

    /**
     * 获取用户列表
     * @param page 页码，默认值为1
     * @param size 每页大小，默认值为10
     * @return 用户分页列表
     */
    @GetMapping("/list")
    @Operation(summary = "用户列表")
    public Result<Page<User>> list(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   @RequestParam(required = false) String username) {
        Page<User> pageParam = new Page<>(page, size);
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User> wrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        if (username != null && !username.isEmpty()) {
            wrapper.like(User::getUsername, username);
        }
        wrapper.orderByDesc(User::getCreateTime);
        return Result.success(userService.page(pageParam, wrapper));
    }

    @GetMapping("/{userId}/logs")
    @Operation(summary = "用户日志")
    public Result<Page<com.shop.entity.UserLog>> getLogs(@PathVariable Long userId,
                                                         @RequestParam(defaultValue = "1") Integer page,
                                                         @RequestParam(defaultValue = "10") Integer size) {
        Page<com.shop.entity.UserLog> pageParam = new Page<>(page, size);
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.shop.entity.UserLog> wrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        wrapper.eq(com.shop.entity.UserLog::getUserId, userId);
        wrapper.orderByDesc(com.shop.entity.UserLog::getCreateTime);
        return Result.success(userLogService.page(pageParam, wrapper));
    }
}