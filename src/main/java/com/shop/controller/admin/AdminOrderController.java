package com.shop.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.entity.OrderMaster;
import com.shop.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

//import java.util.List;

/**
 * 后台订单管理控制器
 * 负责处理后台订单的查询、详情查看和发货等管理操作
 */
@RestController
@RequestMapping("/backend/order")
@Tag(name = "后台-订单管理")
public class AdminOrderController {

    @org.springframework.beans.factory.annotation.Autowired
    private com.shop.service.OrderService orderService;  // 订单服务，处理订单相关业务逻辑

    /**
     * 获取所有订单分页列表
     * @param page 页码，默认值为1
     * @param size 每页大小，默认值为10
     * @return 订单分页数据
     */
    @GetMapping("/list")
    @Operation(summary = "所有订单列表")
    public Result<Page<OrderMaster>> list(@RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "10") Integer size) {
        // 创建分页参数对象
        Page<OrderMaster> pageParam = new Page<>(page, size);
        // 创建查询条件构造器，按创建时间倒序排序
        LambdaQueryWrapper<OrderMaster> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(OrderMaster::getCreateTime);
        // 调用订单服务获取分页数据
        Page<OrderMaster> result = orderService.page(pageParam, wrapper);
        return Result.success(result);
    }

    /**
     * 根据订单编号获取订单详情
     * @param orderNo 订单编号
     * @return 订单详情信息
     */
    @GetMapping("/{orderNo}")
    @Operation(summary = "订单详情")
    public Result<OrderMaster> detail(@PathVariable String orderNo) {
        // 创建查询条件构造器，根据订单编号查询
        LambdaQueryWrapper<OrderMaster> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderMaster::getOrderNo, orderNo);
        // 调用订单服务获取订单详情
        OrderMaster orderMaster = orderService.getOne(wrapper);
        return Result.success(orderMaster);
    }

    /**
     * 处理订单发货操作
     * @param orderNo 订单编号
     * @return 操作结果
     */
    @PostMapping("/send/{orderNo}")
    @Operation(summary = "发货")
    public Result<String> send(@PathVariable String orderNo) {
        // 调用订单服务执行发货操作
        orderService.send(orderNo);
        return Result.success("发货成功");
    }
}