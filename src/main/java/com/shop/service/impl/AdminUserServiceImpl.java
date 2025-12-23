package com.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.dto.UserLoginDTO;
import com.shop.entity.AdminUser;
import com.shop.repository.AdminUserMapper;
import com.shop.service.AdminUserService;
import com.shop.utils.JwtUtil;
import com.shop.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 管理员用户服务实现类
 * 实现管理员用户相关的业务逻辑
 */
@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {

    // 注入JWT工具类，用于生成令牌
    @Autowired
    private JwtUtil jwtUtil;

    // 注入密码加密器，用于密码验证
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 管理员登录实现
     * @param loginDTO 登录信息，包含用户名和密码
     * @return 登录结果，成功时返回JWT令牌
     */
    @Override
    public Result<String> login(UserLoginDTO loginDTO) {
        // 根据用户名查询管理员信息
        LambdaQueryWrapper<AdminUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminUser::getUsername, loginDTO.getUsername());
        AdminUser user = this.getOne(wrapper);

        // 验证管理员是否存在
        if (user == null) {
            return Result.error("用户名或密码错误");
        }

        // 验证密码是否正确
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return Result.error("用户名或密码错误");
        }

        // 生成JWT令牌并返回
        String token = jwtUtil.generateToken(user.getUsername(), "ADMIN");
        return Result.success(token);
    }
}