package com.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.entity.UserLog;
import com.shop.repository.UserLogMapper;
import com.shop.service.UserLogService;
import org.springframework.stereotype.Service;

@Service
public class UserLogServiceImpl extends ServiceImpl<UserLogMapper, UserLog> implements UserLogService {

    @Override
    public void logView(Long userId, Long productId) {
        UserLog log = new UserLog();
        log.setUserId(userId);
        log.setActionType("VIEW");
        log.setTargetId(productId);
        log.setDetails("View Product " + productId);
        this.save(log);
    }

    @Override
    public void logBuy(Long userId, Long orderId, String details) {
        UserLog log = new UserLog();
        log.setUserId(userId);
        log.setActionType("BUY");
        log.setTargetId(orderId);
        log.setDetails(details);
        this.save(log);
    }
}
