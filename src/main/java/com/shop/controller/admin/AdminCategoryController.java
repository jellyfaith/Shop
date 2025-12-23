package com.shop.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.entity.ProductCategory;
import com.shop.service.ProductCategoryService;
import com.shop.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/backend/category")
@Tag(name = "后台-分类管理")
public class
AdminCategoryController {

    @Autowired
    private ProductCategoryService categoryService;

    @GetMapping("/list")
    @Operation(summary = "分类列表")
    public Result<List<ProductCategory>> list() {
        LambdaQueryWrapper<ProductCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(ProductCategory::getSort);
        return Result.success(categoryService.list(wrapper));
    }

    @GetMapping("/page")
    @Operation(summary = "分类分页")
    public Result<Page<ProductCategory>> page(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer size) {
        Page<ProductCategory> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<ProductCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(ProductCategory::getSort);
        return Result.success(categoryService.page(pageParam, wrapper));
    }

    @PostMapping("/add")
    @Operation(summary = "添加分类")
    public Result<String> add(@RequestBody ProductCategory category) {
        categoryService.save(category);
        return Result.success("添加成功");
    }

    @PutMapping("/update")
    @Operation(summary = "更新分类")
    public Result<String> update(@RequestBody ProductCategory category) {
        categoryService.updateById(category);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类")
    public Result<String> delete(@PathVariable Long id) {
        categoryService.removeById(id);
        return Result.success("删除成功");
    }
}
