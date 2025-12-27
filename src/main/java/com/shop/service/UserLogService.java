package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.entity.UserLog;

public interface UserLogService extends IService<UserLog> {
    void logView(Long userId, Long productId);
    void logBuy(Long userId, Long orderId, String details);
}
