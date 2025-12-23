package com.shop.service.impl;

import com.shop.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * 邮件服务实现类
 * 用于发送简单的文本邮件，实现了EmailService接口
 */
@Service
public class EmailServiceImpl implements EmailService {

    // Java邮件发送器，用于实际发送邮件
    @Autowired
    private JavaMailSender emailSender;

    // 发件人邮箱地址，从配置文件中读取
    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送简单文本邮件
     * @param to 收件人邮箱地址
     * @param subject 邮件主题
     * @param text 邮件正文内容
     */
    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            // 创建简单邮件消息对象
            SimpleMailMessage message = new SimpleMailMessage();
            // 设置发件人邮箱
            message.setFrom(from);
            // 设置收件人邮箱
            message.setTo(to);
            // 设置邮件主题
            message.setSubject(subject);
            // 设置邮件正文
            message.setText(text);
            // 发送邮件
            emailSender.send(message);
        } catch (Exception e) {
            // 打印异常堆栈信息
            e.printStackTrace();
            // 记录错误日志但不中断流程
            System.err.println("发送邮件失败，收件人: " + to + "，错误信息: " + e.getMessage());
        }
    }
}