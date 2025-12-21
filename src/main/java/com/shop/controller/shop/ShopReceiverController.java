package com.shop.controller.shop;

import com.shop.entity.Receiver;
import com.shop.entity.User;
import com.shop.service.ReceiverService;
import com.shop.service.UserService;
import com.shop.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop/receiver")
@Tag(name = "商城-收货地址")
public class ShopReceiverController {

    @Autowired
    private ReceiverService receiverService;

    @Autowired
    private UserService userService;

    private Long getUserId(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        if (username == null) return null;
        User user = userService.lambdaQuery().eq(User::getUsername, username).one();
        return user != null ? user.getId() : null;
    }

    @GetMapping("/list")
    @Operation(summary = "获取收货地址列表")
    public Result<List<Receiver>> list(HttpServletRequest request) {
        Long userId = getUserId(request);
        if (userId == null) return Result.error("未登录");
        return Result.success(receiverService.lambdaQuery().eq(Receiver::getUserId, userId).orderByDesc(Receiver::getIsDefault).list());
    }

    @PostMapping("/add")
    @Operation(summary = "添加收货地址")
    public Result<String> add(@RequestBody Receiver receiver, HttpServletRequest request) {
        Long userId = getUserId(request);
        if (userId == null) return Result.error("未登录");
        receiver.setUserId(userId);
        receiverService.save(receiver);
        return Result.success("添加成功");
    }

    @PutMapping("/update")
    @Operation(summary = "更新收货地址")
    public Result<String> update(@RequestBody Receiver receiver, HttpServletRequest request) {
        Long userId = getUserId(request);
        if (userId == null) return Result.error("未登录");
        // Ensure user owns this receiver
        Receiver dbReceiver = receiverService.getById(receiver.getId());
        if (dbReceiver == null || !dbReceiver.getUserId().equals(userId)) {
            return Result.error("地址不存在或无权修改");
        }
        receiver.setUserId(userId); // Prevent changing owner
        receiverService.updateById(receiver);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除收货地址")
    public Result<String> delete(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserId(request);
        if (userId == null) return Result.error("未登录");
        Receiver dbReceiver = receiverService.getById(id);
        if (dbReceiver != null && dbReceiver.getUserId().equals(userId)) {
            receiverService.removeById(id);
        }
        return Result.success("删除成功");
    }

    @PostMapping("/default/{id}")
    @Operation(summary = "设为默认地址")
    public Result<String> setDefault(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserId(request);
        if (userId == null) return Result.error("未登录");
        receiverService.setDefault(userId, id);
        return Result.success("设置成功");
    }
}
