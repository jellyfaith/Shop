package com.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.entity.Receiver;
import com.shop.repository.ReceiverMapper;
import com.shop.service.ReceiverService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 收货地址服务实现类
 * 
 * <p>该类是收货地址服务的具体实现，继承自MyBatis Plus提供的ServiceImpl类，
 * 实现了ReceiverService接口。通过继承ServiceImpl，该类自动获得了
 * 大量的CRUD（增删改查）操作方法，同时添加了自定义的业务逻辑。</p>
 * 
 * <p>主要功能包括：
 * <ul>
 *   <li>基础的收货地址CRUD操作（继承自ServiceImpl）</li>
 *   <li>设置默认收货地址的业务逻辑</li>
 * </ul>
 * </p>
 */
@Service
public class ReceiverServiceImpl extends ServiceImpl<ReceiverMapper, Receiver> implements ReceiverService {

    /**
     * 设置默认收货地址
     * 
     * <p>该方法用于将指定用户的某个收货地址设置为默认地址。
     * 实现逻辑采用事务处理，确保数据的一致性：
     * <ol>
     *   <li>首先将该用户的所有收货地址都设置为非默认</li>
     *   <li>然后将指定的收货地址设置为默认</li>
     * </ol>
     * </p>
     * 
     * @param userId 用户ID，用于标识属于哪个用户的收货地址
     * @param receiverId 收货地址ID，要设置为默认的收货地址
     */
    @Override
    @Transactional
    public void setDefault(Long userId, Long receiverId) {
        // 1. 将该用户的所有收货地址设置为非默认
        LambdaUpdateWrapper<Receiver> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Receiver::getUserId, userId) // 条件：用户ID等于给定的userId
                    .set(Receiver::getIsDefault, 0);  // 更新：将isDefault字段设置为0（非默认）
        this.update(updateWrapper);

        // 2. 将指定的收货地址设置为默认
        LambdaUpdateWrapper<Receiver> defaultWrapper = new LambdaUpdateWrapper<>();
        defaultWrapper.eq(Receiver::getId, receiverId)      // 条件1：收货地址ID等于给定的receiverId
                      .eq(Receiver::getUserId, userId)       // 条件2：用户ID等于给定的userId（双重校验，确保安全性）
                      .set(Receiver::getIsDefault, 1);        // 更新：将isDefault字段设置为1（默认）
        this.update(defaultWrapper);
    }
}