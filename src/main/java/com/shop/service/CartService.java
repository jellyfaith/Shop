package com.shop.service;

import com.shop.dto.CartAddDTO;
import com.shop.vo.CartVO;

public interface CartService {
    void add(String username, CartAddDTO cartAddDTO);
    CartVO list(String username);
    void update(String username, Long productId, Integer quantity);
    void delete(String username, Long productId);
}
