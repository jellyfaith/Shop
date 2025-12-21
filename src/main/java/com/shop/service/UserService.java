package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.dto.UserLoginDTO;
import com.shop.dto.UserRegisterDTO;
import com.shop.entity.User;
import com.shop.utils.Result;

public interface UserService extends IService<User> {
    Result<String> login(UserLoginDTO loginDTO);
    Result<String> register(UserRegisterDTO registerDTO);
    Result<String> update(User user);
}
