package com.shop.controller.admin;

import com.shop.entity.OrderMaster;
import com.shop.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/backend/order")
@Tag(name = "后台-订单管理")
public class AdminOrderController {

    @GetMapping("/list")
    @Operation(summary = "所有订单列表")
    public Result<List<OrderMaster>> list(@RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "10") Integer size) {
        // TODO: Implement admin list logic
        return Result.success();
    }

    @GetMapping("/{orderNo}")
    @Operation(summary = "订单详情")
    public Result<OrderMaster> detail(@PathVariable String orderNo) {
        // TODO: Implement detail logic
        return Result.success();
    }

    @PostMapping("/send/{orderNo}")
    @Operation(summary = "发货")
    public Result<String> send(@PathVariable String orderNo) {
        // TODO: Implement send logic
        return Result.success("发货成功");
    }
}
