package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.dto.UserLoginDTO;
import com.shop.dto.UserRegisterDTO;
import com.shop.entity.User;
import com.shop.utils.Result;

import java.util.List;

/**
 * 用户服务接口
 * 继承自MyBatis-Plus的IService接口，提供基本的CRUD操作
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     * 
     * @param loginDTO 登录数据传输对象，包含用户名和密码等登录信息
     * @return 登录结果，成功时返回包含token的Result对象
     */
    Result<String> login(UserLoginDTO loginDTO);

    /**
     * 用户注册
     * 
     * @param registerDTO 注册数据传输对象，包含用户名、密码、邮箱等注册信息
     * @return 注册结果，成功时返回包含提示信息的Result对象
     */
    Result<String> register(UserRegisterDTO registerDTO);

    /**
     * 更新用户信息
     * 
     * @param user 包含更新信息的用户对象
     * @return 更新结果，成功时返回包含提示信息的Result对象
     */
    Result<String> update(User user);

    /**
     * 统计指定日期范围内的用户数量
     * 
     * @param start 开始日期时间
     * @param end   结束日期时间
     * @return 指定日期范围内的用户数量
     */
    long countUsersByDateRange(java.time.LocalDateTime start, java.time.LocalDateTime end);

    /**
     * 获取最近注册的用户列表
     * 
     * @param limit 限制返回的用户数量
     * @return 最近注册的用户列表
     */
    List<User> findRecentUsers(int limit);
}