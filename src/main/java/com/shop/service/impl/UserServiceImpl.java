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

/**
 * 用户服务实现类
 * 
 * <p>该类是用户服务的具体实现，继承自MyBatis Plus提供的ServiceImpl类，
 * 实现了UserService接口。主要负责处理用户的注册、登录、信息更新等业务逻辑，
 * 并使用JWT进行身份认证，BCrypt进行密码加密。</p>
 * 
 * <p>主要功能包括：
 * <ul>
 *   <li>用户注册（包含密码复杂度验证）</li>
 *   <li>用户登录（JWT令牌生成）</li>
 *   <li>用户信息更新</li>
 *   <li>用户统计查询</li>
 *   <li>用户列表查询</li>
 * </ul>
 * </p>
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    // JWT工具类，用于生成和验证JSON Web Token
    @Autowired
    private JwtUtil jwtUtil;

    // 密码编码器，用于BCrypt加密和验证密码
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 用户登录方法
     * 
     * <p>该方法用于验证用户登录信息，并生成JWT令牌。
     * 实现逻辑：
     * 
     *   根据用户名查询用户信息
     *   验证用户是否存在
     *   使用BCrypt验证密码是否匹配
     *   生成JWT令牌并返回
     * 
     * @param loginDTO 登录请求数据传输对象，包含用户名和密码
     * @return 登录结果，成功返回JWT令牌，失败返回错误信息
     */
    @Override
    public Result<String> login(UserLoginDTO loginDTO) {
        // 构建查询条件，根据用户名查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = this.getOne(wrapper);

        // 验证用户是否存在
        if (user == null) {
            return Result.error("用户名或密码错误");
        }

        // 使用BCrypt加密算法验证密码是否匹配
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return Result.error("用户名或密码错误");
        }

        // 生成JWT令牌，包含用户名和用户角色
        String token = jwtUtil.generateToken(user.getUsername(), "USER");
        return Result.success(token);
    }

    /**
     * 用户注册方法
     * 
     * <p>该方法用于处理用户注册请求，包含密码复杂度验证和用户名唯一性检查。
     * 实现逻辑：
     * <ol>
     *   <li>验证密码复杂度</li>
     *   <li>检查用户名是否已存在</li>
     *   <li>复制注册信息到用户实体</li>
     *   <li>使用BCrypt加密密码</li>
     *   <li>保存用户信息到数据库</li>
     * </ol>
     * </p>
     * 
     * @param registerDTO 注册请求数据传输对象，包含用户名、密码等信息
     * @return 注册结果，成功返回成功信息，失败返回错误信息
     */
    @Override
    public Result<String> register(UserRegisterDTO registerDTO) {
        // 打印注册请求日志
        System.out.println("收到注册请求: " + registerDTO.getUsername());
        
        // 校验密码复杂度
        if (!validatePassword(registerDTO.getPassword())) {
            System.out.println("注册失败: 密码复杂度不符合要求");
            return Result.error("密码必须为8-20位，且包含字母、数字、符号中的至少两种");
        }

        // 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, registerDTO.getUsername());
        if (this.count(wrapper) > 0) {
            System.out.println("注册失败: 用户名已存在");
            return Result.error("用户名已存在");
        }

        // 创建用户实体并复制注册信息
        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);
        
        // 使用BCrypt加密密码
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        
        // 保存用户信息到数据库
        this.save(user);
        System.out.println("注册成功: " + user.getId());
        
        return Result.success("注册成功");
    }

    /**
     * 密码复杂度验证方法
     * 
     * <p>该方法用于验证密码是否符合复杂度要求：
     * <ul>
     *   <li>密码长度必须在8-20位之间</li>
     *   <li>必须包含字母、数字、符号中的至少两种类型</li>
     * </ul>
     * </p>
     * 
     * @param password 要验证的密码
     * @return 密码是否符合复杂度要求，符合返回true，否则返回false
     */
    private boolean validatePassword(String password) {
        // 检查密码是否为空或长度不符合要求
        if (password == null || password.length() < 8 || password.length() > 20) {
            return false;
        }
        
        // 统计密码包含的字符类型数量
        int types = 0;
        if (password.matches(".*[a-zA-Z].*")) types++; // 包含字母
        if (password.matches(".*\\d.*")) types++;     // 包含数字
        if (password.matches(".*[^a-zA-Z0-9].*")) types++; // 包含符号
        
        // 至少包含两种字符类型
        return types >= 2;
    }

    /**
     * 更新用户信息方法
     * 
     * <p>该方法用于更新用户的信息。
     * </p>
     * 
     * @param user 包含更新信息的用户实体
     * @return 更新结果，成功返回成功信息，失败返回错误信息
     */
    @Override
    public Result<String> update(User user) {
        // 根据用户ID更新用户信息
        boolean success = this.updateById(user);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }

    /**
     * 统计指定日期范围内的用户数量
     * 
     * <p>该方法用于统计在指定开始时间和结束时间之间注册的用户数量。
     * </p>
     * 
     * @param start 开始时间
     * @param end 结束时间
     * @return 指定日期范围内的用户数量
     */
    @Override
    public long countUsersByDateRange(java.time.LocalDateTime start, java.time.LocalDateTime end) {
        // 构建查询条件，筛选创建时间在指定范围内的用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(User::getCreateTime, start)  // 大于等于开始时间
               .lt(User::getCreateTime, end);    // 小于结束时间
        
        // 统计符合条件的用户数量
        return this.count(wrapper);
    }

    /**
     * 查找最近注册的用户
     * 
     * <p>该方法用于查找最近注册的指定数量的用户。
     * </p>
     * 
     * @param limit 要查询的用户数量限制
     * @return 最近注册的用户列表
     */
    @Override
    public java.util.List<User> findRecentUsers(int limit) {
        // 构建查询条件，按创建时间降序排序并限制结果数量
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(User::getCreateTime)  // 按创建时间降序排序
               .last("LIMIT " + limit);            // 使用SQL LIMIT子句限制结果数量
        
        // 查询并返回符合条件的用户列表
        return this.list(wrapper);
    }
}