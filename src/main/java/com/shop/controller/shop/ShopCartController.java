package com.shop.controller.shop;

import com.shop.dto.CartAddDTO;
import com.shop.service.CartService;
import com.shop.utils.Result;
import com.shop.vo.CartVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商城购物车接口
 */
@RestController
@RequestMapping("/shop/cart")
@Tag(name = "商城-购物车")
public class ShopCartController {

    @Autowired
    private CartService cartService;

    private String getUsername(HttpServletRequest request) {
        // Assuming JwtInterceptor sets the "username" attribute
        return (String) request.getAttribute("username");
    }

    /**
     * 获取当前用户的购物车列表
     *
     * @param request HTTP请求
     * @return 购物车信息
     */
    @GetMapping
    @Operation(summary = "获取购物车列表")
    public Result<CartVO> list(HttpServletRequest request) {
        String username = getUsername(request);
        if (username == null) {
            return Result.error("未登录");
        }
        return Result.success(cartService.list(username));
    }

    /**
     * 添加商品到购物车
     *
     * @param cartAddDTO 购物车添加信息
     * @param request HTTP请求
     * @return 操作结果
     */
    @PostMapping
    @Operation(summary = "添加商品到购物车")
    public Result<String> add(@RequestBody CartAddDTO cartAddDTO, HttpServletRequest request) {
        String username = getUsername(request);
        if (username == null) {
            return Result.error("未登录");
        }
        cartService.add(username, cartAddDTO);
        return Result.success("添加成功");
    }

    /**
     * 更新购物车中商品的数量
     *
     * @param cartAddDTO 购物车更新信息
     * @param request HTTP请求
     * @return 操作结果
     */
    @PutMapping
    @Operation(summary = "更新购物车商品数量")
    public Result<String> update(@RequestBody CartAddDTO cartAddDTO, HttpServletRequest request) {
        String username = getUsername(request);
        if (username == null) {
            return Result.error("未登录");
        }
        cartService.update(username, cartAddDTO.getProductId(), cartAddDTO.getQuantity());
        return Result.success("更新成功");
    }

    /**
     * 从购物车中删除商品
     *
     * @param productId 商品ID
     * @param request HTTP请求
     * @return 操作结果
     */
    @DeleteMapping("/{productId}")
    @Operation(summary = "删除购物车商品")
    public Result<String> delete(@PathVariable Long productId, HttpServletRequest request) {
        String username = getUsername(request);
        if (username == null) {
            return Result.error("未登录");
        }
        cartService.delete(username, productId);
        return Result.success("删除成功");
    }
}
