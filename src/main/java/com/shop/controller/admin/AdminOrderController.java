package com.shop.controller.admin;

import com.shop.entity.OrderMaster;
import com.shop.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台订单管理接口
 */
@RestController
@RequestMapping("/backend/order")
@Tag(name = "后台-订单管理")
public class AdminOrderController {

    /**
     * 获取所有订单列表
     *
     * @param page 页码
     * @param size 每页大小
     * @return 订单列表
     */
    @GetMapping("/list")
    @Operation(summary = "所有订单列表")
    public Result<List<OrderMaster>> list(@RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "10") Integer size) {
        // TODO: Implement admin list logic
        return Result.success();
    }

    /**
     * 获取订单详情
     *
     * @param orderNo 订单编号
     * @return 订单详情
     */
    @GetMapping("/{orderNo}")
    @Operation(summary = "订单详情")
    public Result<OrderMaster> detail(@PathVariable String orderNo) {
        // TODO: Implement detail logic
        return Result.success();
    }

    /**
     * 订单发货
     *
     * @param orderNo 订单编号
     * @return 操作结果
     */
    @PostMapping("/send/{orderNo}")
    @Operation(summary = "发货")
    public Result<String> send(@PathVariable String orderNo) {
        // TODO: Implement send logic
        return Result.success("发货成功");
    }
}
