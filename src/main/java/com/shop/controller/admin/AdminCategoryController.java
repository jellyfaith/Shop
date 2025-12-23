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

/**
 * 后台商品分类管理控制器
 * 负责处理商品分类的增删改查等后台管理操作
 */
@RestController
@RequestMapping("/backend/category") // 接口基础路径
@Tag(name = "后台-分类管理") // Swagger文档标签
public class AdminCategoryController {

    @Autowired
    private ProductCategoryService categoryService; // 商品分类服务层依赖注入

    /**
     * 获取所有分类列表
     * 按分类排序字段升序排列
     * @return 分类列表数据
     */
    @GetMapping("/list")
    @Operation(summary = "获取所有分类列表")
    public Result<List<ProductCategory>> list() {
        // 创建查询条件包装器
        LambdaQueryWrapper<ProductCategory> wrapper = new LambdaQueryWrapper<>();
        // 按排序字段升序排列
        wrapper.orderByAsc(ProductCategory::getSort);
        // 调用服务层获取分类列表并返回成功结果
        return Result.success(categoryService.list(wrapper));
    }

    /**
     * 获取分类分页列表
     * @param page 页码，默认1
     * @param size 每页大小，默认10
     * @return 分类分页数据
     */
    @GetMapping("/page")
    @Operation(summary = "获取分类分页列表")
    public Result<Page<ProductCategory>> page(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer size) {
        // 创建分页参数对象
        Page<ProductCategory> pageParam = new Page<>(page, size);
        // 创建查询条件包装器
        LambdaQueryWrapper<ProductCategory> wrapper = new LambdaQueryWrapper<>();
        // 按排序字段升序排列
        wrapper.orderByAsc(ProductCategory::getSort);
        // 调用服务层进行分页查询并返回结果
        return Result.success(categoryService.page(pageParam, wrapper));
    }

    /**
     * 添加商品分类
     * @param category 分类信息
     * @return 添加结果
     */
    @PostMapping("/add")
    @Operation(summary = "添加商品分类")
    public Result<String> add(@RequestBody ProductCategory category) {
        // 调用服务层保存分类信息
        categoryService.save(category);
        // 返回添加成功结果
        return Result.success("添加成功");
    }

    /**
     * 更新商品分类
     * @param category 更新后的分类信息
     * @return 更新结果
     */
    @PutMapping("/update")
    @Operation(summary = "更新商品分类")
    public Result<String> update(@RequestBody ProductCategory category) {
        // 调用服务层更新分类信息
        categoryService.updateById(category);
        // 返回更新成功结果
        return Result.success("更新成功");
    }

    /**
     * 删除商品分类
     * @param id 分类ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品分类")
    public Result<String> delete(@PathVariable Long id) {
        // 调用服务层删除分类
        categoryService.removeById(id);
        // 返回删除成功结果
        return Result.success("删除成功");
    }
}