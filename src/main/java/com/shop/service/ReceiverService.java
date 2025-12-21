package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.entity.Receiver;

public interface ReceiverService extends IService<Receiver> {
    void setDefault(Long userId, Long receiverId);
}
