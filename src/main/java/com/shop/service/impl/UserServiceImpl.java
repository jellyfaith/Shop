package com.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.dto.UserLoginDTO;
import com.shop.dto.UserRegisterDTO;
import com.shop.entity.User;
import com.shop.repository.UserMapper;
import com.shop.service.UserService;
import com.shop.utils.JwtUtil;
import com.shop.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Result<String> login(UserLoginDTO loginDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = this.getOne(wrapper);

        if (user == null) {
            return Result.error("用户名或密码错误");
        }

        // BCrypt加密对比
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return Result.error("用户名或密码错误");
        }

        String token = jwtUtil.generateToken(user.getUsername(), "USER");
        return Result.success(token);
    }

    @Override
    public Result<String> register(UserRegisterDTO registerDTO) {
        System.out.println("收到注册请求: " + registerDTO.getUsername());
        // 校验密码复杂度
        if (!validatePassword(registerDTO.getPassword())) {
            System.out.println("注册失败: 密码复杂度不符合要求");
            return Result.error("密码必须为8-20位，且包含字母、数字、符号中的至少两种");
        }

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, registerDTO.getUsername());
        if (this.count(wrapper) > 0) {
            System.out.println("注册失败: 用户名已存在");
            return Result.error("用户名已存在");
        }

        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);
        // BCrypt加密
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        
        this.save(user);
        System.out.println("注册成功: " + user.getId());
        return Result.success("注册成功");
    }

    private boolean validatePassword(String password) {
        if (password == null || password.length() < 8 || password.length() > 20) {
            return false;
        }
        int types = 0;
        if (password.matches(".*[a-zA-Z].*")) types++;
        if (password.matches(".*\\d.*")) types++;
        if (password.matches(".*[^a-zA-Z0-9].*")) types++; // 符号
        return types >= 2;
    }

    @Override
    public Result<String> update(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        User dbUser = this.getOne(wrapper);
        if (dbUser == null) {
            return Result.error("用户不存在");
        }
        
        // Update fields
        if (user.getEmail() != null) dbUser.setEmail(user.getEmail());
        if (user.getPhone() != null) dbUser.setPhone(user.getPhone());
        if (user.getAddress() != null) dbUser.setAddress(user.getAddress());
        
        this.updateById(dbUser);
        return Result.success("更新成功");
    }
}
