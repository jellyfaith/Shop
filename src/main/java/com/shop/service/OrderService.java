package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.dto.OrderCreateDTO;
import com.shop.entity.OrderMaster;
import com.shop.vo.OrderVO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 订单服务接口
 * 继承自MyBatis-Plus的IService接口，提供基本的CRUD操作
 */
public interface OrderService extends IService<OrderMaster> {

    /**
     * 创建订单
     * 
     * @param username      用户名，标识下单用户
     * @param orderCreateDTO 创建订单的数据传输对象，包含订单相关信息
     * @return 生成的订单号
     */
    String create(String username, OrderCreateDTO orderCreateDTO);

    /**
     * 获取用户的订单列表
     * 
     * @param username 用户名，标识查询的用户
     * @return 用户的订单列表，以OrderVO形式返回
     */
    List<OrderVO> list(String username);

    /**
     * 获取订单详情
     * 
     * @param username 用户名，用于验证订单归属
     * @param orderNo  订单号，标识要查询的订单
     * @return 订单详情，以OrderVO形式返回
     */
    OrderVO detail(String username, String orderNo);

    /**
     * 订单支付
     * 
     * @param username 用户名，用于验证订单归属
     * @param orderNo  订单号，标识要支付的订单
     */
    void pay(String username, String orderNo);

    /**
     * 订单发货
     * （仅管理员可用）
     * 
     * @param orderNo 订单号，标识要发货的订单
     */
    void send(String orderNo); // Admin only
    
    // Stats 统计相关方法

    /**
     * 获取总销售额
     * 
     * @return 总销售额，以BigDecimal形式返回
     */
    BigDecimal getTotalSales();

    /**
     * 统计待处理订单数量
     * 
     * @return 待处理订单数量
     */
    long countPendingOrders();

    /**
     * 获取指定日期范围内的销售额
     * 
     * @param start 开始日期时间
     * @param end   结束日期时间
     * @return 指定日期范围内的销售额
     */
    BigDecimal getSalesByDateRange(java.time.LocalDateTime start, java.time.LocalDateTime end);

    /**
     * 统计指定日期范围内的订单数量
     * 
     * @param start 开始日期时间
     * @param end   结束日期时间
     * @return 指定日期范围内的订单数量
     */
    long countOrdersByDateRange(java.time.LocalDateTime start, java.time.LocalDateTime end);

    /**
     * 统计指定日期范围内的待处理订单数量
     * 
     * @param start 开始日期时间
     * @param end   结束日期时间
     * @return 指定日期范围内的待处理订单数量
     */
    long countPendingOrdersByDateRange(java.time.LocalDateTime start, java.time.LocalDateTime end);

    /**
     * 获取最近的订单列表
     * 
     * @param limit 限制返回的订单数量
     * @return 最近的订单列表
     */
    List<OrderMaster> findRecentOrders(int limit);

    /**
     * 获取指定日期范围内的每日销售额
     * 
     * @param start 开始日期时间
     * @param end   结束日期时间
     * @return 以日期为键、销售额为值的Map
     */
    Map<String, BigDecimal> getDailySales(java.time.LocalDateTime start, java.time.LocalDateTime end);
}