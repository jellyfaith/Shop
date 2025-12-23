package com.shop.controller.admin;

import com.shop.service.OrderService;
import com.shop.service.UserService;
import com.shop.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.entity.OrderMaster;
import com.shop.entity.User;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
//import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 后台仪表盘控制器
 * 负责提供系统统计数据、趋势分析和最近活动等仪表盘功能
 */
@RestController
@RequestMapping("/backend/dashboard")
@Tag(name = "后台-仪表盘")
public class AdminDashboardController {

    @Autowired
    private OrderService orderService;  // 订单服务，用于获取订单相关统计数据

    @Autowired
    private UserService userService;    // 用户服务，用于获取用户相关统计数据

    /**
     * 获取仪表盘统计数据
     * @return 包含各类统计信息的Result对象
     */
    @GetMapping("/stats")
    @Operation(summary = "获取统计数据")
    public Result<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 1. 总销售额 (已支付订单)
        BigDecimal totalSales = orderService.getTotalSales();
        stats.put("totalSales", totalSales != null ? totalSales : BigDecimal.ZERO);
        
        // 2. 订单总数
        long totalOrders = orderService.count();
        stats.put("totalOrders", totalOrders);
        
        // 3. 用户总数
        long totalUsers = userService.count();
        stats.put("totalUsers", totalUsers);
        
        // 4. 待处理订单 (状态为0-待付款 或 1-已付款待发货)
        long pendingOrders = orderService.countPendingOrders();
        stats.put("pendingOrders", pendingOrders);

        // 计算趋势数据 (当月 vs 上月)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfCurrentMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime startOfNextMonth = startOfCurrentMonth.plusMonths(1);
        LocalDateTime startOfLastMonth = startOfCurrentMonth.minusMonths(1);

        // 销售额趋势
        BigDecimal currentMonthSales = orderService.getSalesByDateRange(startOfCurrentMonth, startOfNextMonth);
        BigDecimal lastMonthSales = orderService.getSalesByDateRange(startOfLastMonth, startOfCurrentMonth);
        stats.put("salesTrend", calculateTrend(currentMonthSales, lastMonthSales));

        // 订单数趋势
        long currentMonthOrders = orderService.countOrdersByDateRange(startOfCurrentMonth, startOfNextMonth);
        long lastMonthOrders = orderService.countOrdersByDateRange(startOfLastMonth, startOfCurrentMonth);
        stats.put("ordersTrend", calculateTrend(currentMonthOrders, lastMonthOrders));

        // 用户数趋势
        long currentMonthUsers = userService.countUsersByDateRange(startOfCurrentMonth, startOfNextMonth);
        long lastMonthUsers = userService.countUsersByDateRange(startOfLastMonth, startOfCurrentMonth);
        stats.put("usersTrend", calculateTrend(currentMonthUsers, lastMonthUsers));

        // 待处理订单趋势
        long currentMonthPending = orderService.countPendingOrdersByDateRange(startOfCurrentMonth, startOfNextMonth);
        long lastMonthPending = orderService.countPendingOrdersByDateRange(startOfLastMonth, startOfCurrentMonth);
        stats.put("pendingOrdersTrend", calculateTrend(currentMonthPending, lastMonthPending));

        // 销售图表数据 (最近7天)
        LocalDateTime sevenDaysAgo = now.minusDays(6).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfToday = now.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        Map<String, BigDecimal> dailySales = orderService.getDailySales(sevenDaysAgo, endOfToday);
        
        List<String> chartDates = new ArrayList<>();
        List<BigDecimal> chartValues = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // 构建最近7天的日期和销售额数据
        for (int i = 0; i < 7; i++) {
            LocalDateTime date = sevenDaysAgo.plusDays(i);
            String dateStr = date.format(dateFormatter);
            chartDates.add(dateStr);
            chartValues.add(dailySales.getOrDefault(dateStr, BigDecimal.ZERO));
        }
        
        Map<String, Object> chartData = new HashMap<>();
        chartData.put("dates", chartDates);
        chartData.put("values", chartValues);
        stats.put("salesChart", chartData);

        // 最近活动
        List<Map<String, Object>> activities = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // 获取最近订单
        List<OrderMaster> recentOrders = orderService.findRecentOrders(5);
        for (OrderMaster order : recentOrders) {
            if (order.getCreateTime() != null) {
                Map<String, Object> activity = new HashMap<>();
                activity.put("time", order.getCreateTime().format(formatter));
                activity.put("content", "新订单: " + order.getOrderNo());
                activity.put("type", "primary");  // 活动类型，用于前端展示不同样式
                activity.put("timestamp", order.getCreateTime());
                activities.add(activity);
            }
        }

        // 获取最近注册用户
        List<User> recentUsers = userService.findRecentUsers(5);
        for (User user : recentUsers) {
            if (user.getCreateTime() != null) {
                Map<String, Object> activity = new HashMap<>();
                activity.put("time", user.getCreateTime().format(formatter));
                activity.put("content", "新用户: " + user.getUsername());
                activity.put("type", "success");  // 活动类型，用于前端展示不同样式
                activity.put("timestamp", user.getCreateTime());
                activities.add(activity);
            }
        }

        // 按时间戳降序排序并取前10条
        List<Map<String, Object>> sortedActivities = activities.stream()
                .sorted((a, b) -> ((LocalDateTime) b.get("timestamp")).compareTo((LocalDateTime) a.get("timestamp")))
                .limit(10)
                .collect(Collectors.toList());
        
        stats.put("activities", sortedActivities);
        
        return Result.success(stats);
    }

    /**
     * 计算趋势百分比(整数类型)
     * @param current 当前值
     * @param last 上一期值
     * @return 趋势百分比
     */
    private double calculateTrend(long current, long last) {
        if (last == 0) {
            return current > 0 ? 100.0 : 0.0;
        }
        return BigDecimal.valueOf(current - last)
                .divide(BigDecimal.valueOf(last), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }

    /**
     * 计算趋势百分比(BigDecimal类型)
     * @param current 当前值
     * @param last 上一期值
     * @return 趋势百分比
     */
    private double calculateTrend(BigDecimal current, BigDecimal last) {
        if (current == null) current = BigDecimal.ZERO;
        if (last == null) last = BigDecimal.ZERO;
        
        if (last.compareTo(BigDecimal.ZERO) == 0) {
            return current.compareTo(BigDecimal.ZERO) > 0 ? 100.0 : 0.0;
        }
        return current.subtract(last)
                .divide(last, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }
}