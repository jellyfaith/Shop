package com.shop.controller.shop;

import com.shop.dto.OrderCreateDTO;
import com.shop.service.OrderService;
import com.shop.utils.Result;
import com.shop.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商城订单控制器
 * 负责处理商城订单的创建、查询、支付等用户端订单管理功能
 */
@RestController
@RequestMapping("/shop/order")
@Tag(name = "商城-订单管理")
public class ShopOrderController {

    @Autowired
    private OrderService orderService;  // 订单服务，处理订单相关业务逻辑

    /**
     * 从请求中获取当前登录用户的用户名
     * 用户名由JWT拦截器解析并设置到请求属性中
     * @param request HTTP请求对象
     * @return 当前登录用户的用户名
     */
    private String getUsername(HttpServletRequest request) {
        return (String) request.getAttribute("username");
    }

    /**
     * 创建订单
     * @param orderCreateDTO 订单创建信息，包含商品列表、收货地址ID、支付方式等
     * @param request HTTP请求，用于获取当前登录用户信息
     * @return 订单编号
     */
    @PostMapping("/create")
    @Operation(summary = "创建订单")
    public Result<String> create(@RequestBody OrderCreateDTO orderCreateDTO, HttpServletRequest request) {
        String username = getUsername(request);
        if (username == null) return Result.error("未登录");  // 未登录验证
        String orderNo = orderService.create(username, orderCreateDTO);  // 调用服务层创建订单
        return Result.success(orderNo);
    }

    /**
     * 获取当前用户的订单列表
     * @param request HTTP请求，用于获取当前登录用户信息
     * @return 订单列表
     */
    @GetMapping("/list")
    @Operation(summary = "我的订单列表")
    public Result<List<OrderVO>> list(HttpServletRequest request) {
        String username = getUsername(request);
        if (username == null) return Result.error("未登录");  // 未登录验证
        return Result.success(orderService.list(username));  // 调用服务层获取订单列表
    }

    /**
     * 获取订单详情
     * @param orderNo 订单编号
     * @param request HTTP请求，用于获取当前登录用户信息
     * @return 订单详情
     */
    @GetMapping("/{orderNo}")
    @Operation(summary = "订单详情")
    public Result<OrderVO> detail(@PathVariable String orderNo, HttpServletRequest request) {
        String username = getUsername(request);
        if (username == null) return Result.error("未登录");  // 未登录验证
        return Result.success(orderService.detail(username, orderNo));  // 调用服务层获取订单详情
    }

    /**
     * 模拟支付订单
     * 实际项目中应接入真实支付接口，此处仅用于演示
     * @param orderNo 订单编号
     * @param request HTTP请求，用于获取当前登录用户信息
     * @return 操作结果
     */
    @PostMapping("/pay/{orderNo}")
    @Operation(summary = "模拟支付")
    public Result<String> pay(@PathVariable String orderNo, HttpServletRequest request) {
        String username = getUsername(request);
        if (username == null) return Result.error("未登录");  // 未登录验证
        orderService.pay(username, orderNo);  // 调用服务层处理支付逻辑
        return Result.success("支付成功");
    }
}