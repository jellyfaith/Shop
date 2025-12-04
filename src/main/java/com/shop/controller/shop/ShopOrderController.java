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

@RestController
@RequestMapping("/shop/order")
@Tag(name = "商城-订单管理")
public class ShopOrderController {

    @Autowired
    private OrderService orderService;

    private String getUsername(HttpServletRequest request) {
        return (String) request.getAttribute("username");
    }

    @PostMapping("/create")
    @Operation(summary = "创建订单")
    public Result<String> create(@RequestBody OrderCreateDTO orderCreateDTO, HttpServletRequest request) {
        String username = getUsername(request);
        if (username == null) return Result.error("未登录");
        String orderNo = orderService.create(username, orderCreateDTO);
        return Result.success(orderNo);
    }

    @GetMapping("/list")
    @Operation(summary = "我的订单列表")
    public Result<List<OrderVO>> list(HttpServletRequest request) {
        String username = getUsername(request);
        if (username == null) return Result.error("未登录");
        return Result.success(orderService.list(username));
    }

    @GetMapping("/{orderNo}")
    @Operation(summary = "订单详情")
    public Result<OrderVO> detail(@PathVariable String orderNo, HttpServletRequest request) {
        String username = getUsername(request);
        if (username == null) return Result.error("未登录");
        return Result.success(orderService.detail(username, orderNo));
    }

    @PostMapping("/pay/{orderNo}")
    @Operation(summary = "模拟支付")
    public Result<String> pay(@PathVariable String orderNo, HttpServletRequest request) {
        String username = getUsername(request);
        if (username == null) return Result.error("未登录");
        orderService.pay(username, orderNo);
        return Result.success("支付成功");
    }
}
