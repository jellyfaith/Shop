package com.shop.service;

import com.shop.dto.OrderCreateDTO;
import com.shop.vo.OrderVO;
import java.util.List;

public interface OrderService {
    String create(String username, OrderCreateDTO orderCreateDTO);
    List<OrderVO> list(String username);
    OrderVO detail(String username, String orderNo);
    void pay(String username, String orderNo);
    void send(String orderNo); // Admin only
}
