package com.shop.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemVO {
    private Long productId;
    private String productName;
    private String productIcon;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subTotal; // price * quantity
}
