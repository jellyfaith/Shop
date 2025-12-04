package com.shop.dto;

import lombok.Data;

@Data
public class CartAddDTO {
    private Long productId;
    private Integer quantity;
}
