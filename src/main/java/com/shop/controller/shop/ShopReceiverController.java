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

/**
 * 商城收货地址控制器
 * 负责处理用户收货地址的添加、查询、更新、删除和设置默认地址等功能
 */
@RestController
@RequestMapping("/shop/receiver")
@Tag(name = "商城-收货地址")
public class ShopReceiverController {

    @Autowired
    private ReceiverService receiverService;  // 收货地址服务，处理收货地址相关业务逻辑

    @Autowired
    private UserService userService;  // 用户服务，用于根据用户名查询用户信息

    /**
     * 从请求中获取当前登录用户的ID
     * 先从请求属性中获取用户名（由JWT拦截器设置），再根据用户名查询用户ID
     * @param request HTTP请求对象
     * @return 当前登录用户的ID，未登录则返回null
     */
    private Long getUserId(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        if (username == null) return null;  // 未登录
        User user = userService.lambdaQuery().eq(User::getUsername, username).one();  // 根据用户名查询用户
        return user != null ? user.getId() : null;
    }

    /**
     * 获取当前用户的收货地址列表
     * @param request HTTP请求，用于获取当前登录用户信息
     * @return 收货地址列表（默认地址排在前面）
     */
    @GetMapping("/list")
    @Operation(summary = "获取收货地址列表")
    public Result<List<Receiver>> list(HttpServletRequest request) {
        Long userId = getUserId(request);
        if (userId == null) return Result.error("未登录");  // 未登录验证
        // 查询当前用户的所有收货地址，默认地址排在前面
        return Result.success(receiverService.lambdaQuery()
                .eq(Receiver::getUserId, userId)
                .orderByDesc(Receiver::getIsDefault)
                .list());
    }

    /**
     * 添加收货地址
     * @param receiver 收货地址信息
     * @param request HTTP请求，用于获取当前登录用户信息
     * @return 操作结果
     */
    @PostMapping("/add")
    @Operation(summary = "添加收货地址")
    public Result<String> add(@RequestBody Receiver receiver, HttpServletRequest request) {
        Long userId = getUserId(request);
        if (userId == null) return Result.error("未登录");  // 未登录验证
        receiver.setUserId(userId);  // 设置收货地址所属用户ID
        receiverService.save(receiver);  // 保存收货地址
        return Result.success("添加成功");
    }

    /**
     * 更新收货地址
     * @param receiver 收货地址信息（包含ID）
     * @param request HTTP请求，用于获取当前登录用户信息
     * @return 操作结果
     */
    @PutMapping("/update")
    @Operation(summary = "更新收货地址")
    public Result<String> update(@RequestBody Receiver receiver, HttpServletRequest request) {
        Long userId = getUserId(request);
        if (userId == null) return Result.error("未登录");  // 未登录验证
        
        // 验证收货地址是否存在且属于当前用户
        Receiver dbReceiver = receiverService.getById(receiver.getId());
        if (dbReceiver == null || !dbReceiver.getUserId().equals(userId)) {
            return Result.error("地址不存在或无权修改");
        }
        
        receiver.setUserId(userId);  // 防止修改所属用户
        receiverService.updateById(receiver);  // 更新收货地址
        return Result.success("更新成功");
    }

    /**
     * 删除收货地址
     * @param id 收货地址ID
     * @param request HTTP请求，用于获取当前登录用户信息
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除收货地址")
    public Result<String> delete(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserId(request);
        if (userId == null) return Result.error("未登录");  // 未登录验证
        
        // 验证收货地址是否存在且属于当前用户
        Receiver dbReceiver = receiverService.getById(id);
        if (dbReceiver != null && dbReceiver.getUserId().equals(userId)) {
            receiverService.removeById(id);  // 删除收货地址
        }
        
        return Result.success("删除成功");
    }

    /**
     * 设置默认收货地址
     * @param id 收货地址ID
     * @param request HTTP请求，用于获取当前登录用户信息
     * @return 操作结果
     */
    @PostMapping("/default/{id}")
    @Operation(summary = "设为默认地址")
    public Result<String> setDefault(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserId(request);
        if (userId == null) return Result.error("未登录");  // 未登录验证
        
        // 调用服务层设置默认地址（会将该用户其他地址设为非默认）
        receiverService.setDefault(userId, id);
        return Result.success("设置成功");
    }
}