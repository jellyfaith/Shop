package com.shop.service;

/**
 * 邮件服务接口
 * 定义邮件发送相关的业务逻辑方法
 */
public interface EmailService {
    /**
     * 发送简单邮件
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param text 邮件内容
     */
    void sendSimpleMessage(String to, String subject, String text);
}