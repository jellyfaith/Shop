package com.shop.controller.shop;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shop.entity.ProductCategory;
import com.shop.service.ProductCategoryService;
import com.shop.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商城分类控制器
 * 负责处理商品分类相关的前台接口请求
 */
@RestController
@RequestMapping("/category")
@Tag(name = "前台-分类接口")
public class ShopCategoryController {

    @Autowired
    private ProductCategoryService categoryService;  // 商品分类服务，处理分类相关业务逻辑

    /**
     * 获取所有商品分类列表
     * 按分类排序字段升序排列
     * @return 分类列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取所有分类")
    public Result<List<ProductCategory>> list() {
        LambdaQueryWrapper<ProductCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(ProductCategory::getSort);  // 按排序字段升序排列
        return Result.success(categoryService.list(wrapper));
    }
}