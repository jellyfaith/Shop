package com.shop.dto;

import lombok.Data;

/**
 * 创建订单数据传输对象
 * 用于接收前端传递的创建订单请求参数
 */
@Data
public class OrderCreateDTO {
    /**
     * 收货地址ID
     * 可选参数，用于从用户地址簿中选择收货地址
     */
    private Long receiverId; // Optional: Select from address book
    
    /**
     * 收货人姓名
     * 如果未提供receiverId，则需要填写此参数
     */
    private String receiverName;
    
    /**
     * 收货人电话
     * 如果未提供receiverId，则需要填写此参数
     */
    private String receiverPhone;
    
    /**
     * 收货人地址
     * 如果未提供receiverId，则需要填写此参数
     */
    private String receiverAddress;
    
    /**
     * 支付方式
     * 例如："WECHAT"（微信支付）、"ALIPAY"（支付宝支付）
     */
    private String paymentMethod; // e.g., "WECHAT", "ALIPAY"
    
    /**
     * 订单备注
     * 用户对订单的特殊说明或要求
     */
    private String remark;
}