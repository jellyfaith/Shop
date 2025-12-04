package com.shop.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderVO {
    private String orderNo;
    private BigDecimal totalAmount;
    private Integer status; // 0-New, 1-Paid, 2-Shipped, 3-Completed, 4-Cancelled
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private LocalDateTime createTime;
    private List<OrderItemVO> items;
}
