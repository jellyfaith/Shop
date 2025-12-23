package com.shop.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.entity.Product;
import com.shop.entity.ProductSku;
import com.shop.service.ProductService;
import com.shop.service.ProductSkuService;
import com.shop.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台商品管理控制器
 * 负责处理商品的增删改查、状态管理以及SKU相关操作
 */
@RestController
@RequestMapping("/backend/product")
@Tag(name = "后台-商品管理")
public class AdminProductController {

    @Autowired
    private ProductService productService;  // 商品服务，处理商品相关业务逻辑

    @Autowired
    private ProductSkuService productSkuService;  // 商品SKU服务，处理SKU相关业务逻辑

    /**
     * 获取商品分页列表
     * @param page 页码，默认值为1
     * @param size 每页大小，默认值为10
     * @param name 商品名称（可选，支持模糊搜索）
     * @param categoryId 分类ID（可选）
     * @param sortField 排序字段（可选，如 price, stock, sales）
     * @param sortOrder 排序方式（可选，asc, desc）
     * @return 商品分页列表
     */
    @GetMapping("/list")
    @Operation(summary = "商品列表")
    public Result<Page<Product>> list(@RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) Long categoryId,
                                      @RequestParam(required = false) String sortField,
                                      @RequestParam(required = false) String sortOrder) {
        Page<Product> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        
        // 按商品名称模糊搜索
        if (name != null && !name.isEmpty()) {
            wrapper.like(Product::getName, name);
        }
        
        // 按分类ID筛选
        if (categoryId != null) {
            wrapper.eq(Product::getCategoryId, categoryId);
        }
        
        // 排序处理
        if (sortField == null) {
            // 默认按创建时间倒序
            wrapper.orderByDesc(Product::getCreateTime);
        } else {
            boolean isAsc = "asc".equalsIgnoreCase(sortOrder);
            if ("price".equals(sortField)) {
                // 按最低价格排序
                wrapper.orderBy(true, isAsc, Product::getMinPrice);
            } else if ("createTime".equals(sortField)) {
                // 按创建时间排序
                wrapper.orderBy(true, isAsc, Product::getCreateTime);
            }
            // 可根据需要添加更多排序字段
        }
        
        return Result.success(productService.page(pageParam, wrapper));
    }

    /**
     * 添加商品
     * @param product 商品信息
     * @return 操作结果
     */
    @PostMapping("/add")
    @Operation(summary = "添加商品")
    public Result<String> add(@RequestBody Product product) {
        productService.save(product);
        return Result.success("添加成功");
    }

    /**
     * 更新商品信息
     * @param product 商品信息
     * @return 操作结果
     */
    @PutMapping("/update")
    @Operation(summary = "更新商品")
    public Result<String> update(@RequestBody Product product) {
        productService.updateById(product);
        return Result.success("更新成功");
    }

    /**
     * 删除商品
     * @param id 商品ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品")
    public Result<String> delete(@PathVariable Long id) {
        productService.removeById(id);
        return Result.success("删除成功");
    }

    /**
     * 修改商品状态
     * @param id 商品ID
     * @param status 商品状态（0：下架，1：上架）
     * @return 操作结果
     */
    @PutMapping("/status/{id}/{status}")
    @Operation(summary = "修改商品状态")
    public Result<String> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        Product product = new Product();
        product.setId(id);
        product.setStatus(status);
        productService.updateById(product);
        return Result.success("状态更新成功");
    }

    /**
     * 获取商品SKU列表
     * @param productId 商品ID
     * @return SKU列表
     */
    @GetMapping("/skus/{productId}")
    @Operation(summary = "获取商品SKU列表")
    public Result<List<ProductSku>> getSkus(@PathVariable Long productId) {
        LambdaQueryWrapper<ProductSku> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductSku::getProductId, productId);
        return Result.success(productSkuService.list(wrapper));
    }

    /**
     * 更新SKU库存/价格
     * @param sku SKU信息
     * @return 操作结果
     */
    @PutMapping("/sku/update")
    @Operation(summary = "更新SKU库存/价格")
    public Result<String> updateSku(@RequestBody ProductSku sku) {
        productSkuService.updateById(sku);
        return Result.success("更新成功");
    }
}