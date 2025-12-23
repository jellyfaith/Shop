package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.entity.Receiver;

/**
 * 收货地址服务接口
 * 继承自MyBatis-Plus的IService接口，提供收货地址的基本CRUD操作
 */
public interface ReceiverService extends IService<Receiver> {

    /**
     * 设置默认收货地址
     * 
     * @param userId      用户ID，标识要操作的用户
     * @param receiverId  收货地址ID，标识要设置为默认的收货地址
     */
    void setDefault(Long userId, Long receiverId);
}