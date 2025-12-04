package com.shop.controller.shop;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.entity.Product;
import com.shop.service.ProductService;
import com.shop.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 商城商品浏览接口
 */
@RestController
@RequestMapping("/shop/product")
@Tag(name = "商城-商品浏览")
public class ShopProductController {

    @Autowired
    private ProductService productService;

    /**
     * 搜索商品列表
     *
     * @param page 页码
     * @param size 每页大小
     * @param keyword 搜索关键字
     * @param categoryId 分类ID
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 商品分页列表
     */
    @GetMapping("/list")
    @Operation(summary = "商品搜索列表")
    public Result<Page<Product>> list(@RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "20") Integer size,
                                      @RequestParam(required = false) String keyword,
                                      @RequestParam(required = false) Long categoryId,
                                      @RequestParam(required = false) BigDecimal minPrice,
                                      @RequestParam(required = false) BigDecimal maxPrice) {
        Page<Product> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1); // Only active products
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Product::getName, keyword);
        }
        if (categoryId != null) {
            wrapper.eq(Product::getCategoryId, categoryId);
        }
        if (minPrice != null) {
            wrapper.ge(Product::getPrice, minPrice);
        }
        if (maxPrice != null) {
            wrapper.le(Product::getPrice, maxPrice);
        }
        
        wrapper.orderByDesc(Product::getCreateTime);
        return Result.success(productService.page(pageParam, wrapper));
    }

    /**
     * 获取商品详情
     *
     * @param id 商品ID
     * @return 商品详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "商品详情")
    public Result<Product> detail(@PathVariable Long id) {
        return Result.success(productService.getById(id));
    }
}
