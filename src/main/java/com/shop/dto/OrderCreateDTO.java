package com.shop.dto;

import lombok.Data;

@Data
public class OrderCreateDTO {
    private Long receiverId; // Optional: Select from address book
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String paymentMethod; // e.g., "WECHAT", "ALIPAY"
    private String remark;
}
