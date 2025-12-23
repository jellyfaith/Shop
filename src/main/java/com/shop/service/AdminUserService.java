package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.dto.UserLoginDTO;
import com.shop.entity.AdminUser;
import com.shop.utils.Result;

/**
 * 管理员用户服务接口
 * 定义管理员用户相关的业务逻辑方法
 */
public interface AdminUserService extends IService<AdminUser> {
    /**
     * 管理员登录
     * @param loginDTO 登录信息，包含用户名和密码
     * @return 登录结果，成功时返回JWT令牌
     */
    Result<String> login(UserLoginDTO loginDTO);
}