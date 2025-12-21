package com.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.entity.Receiver;
import com.shop.repository.ReceiverMapper;
import com.shop.service.ReceiverService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReceiverServiceImpl extends ServiceImpl<ReceiverMapper, Receiver> implements ReceiverService {

    @Override
    @Transactional
    public void setDefault(Long userId, Long receiverId) {
        // 1. Set all user's receivers to non-default
        LambdaUpdateWrapper<Receiver> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Receiver::getUserId, userId).set(Receiver::getIsDefault, 0);
        this.update(updateWrapper);

        // 2. Set the specific receiver to default
        LambdaUpdateWrapper<Receiver> defaultWrapper = new LambdaUpdateWrapper<>();
        defaultWrapper.eq(Receiver::getId, receiverId).eq(Receiver::getUserId, userId).set(Receiver::getIsDefault, 1);
        this.update(defaultWrapper);
    }
}
