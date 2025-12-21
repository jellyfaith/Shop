package com.shop.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemVO {
    private Long productId;
    private String productName;
    private String productIcon;
    private String specs; // Added specs
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal total;
}
