package com.shop.controller.shop;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shop.entity.Product;
import com.shop.service.ProductService;
import com.shop.utils.Result;
import com.shop.entity.ProductSku;
import com.shop.repository.ProductSkuMapper;
import com.shop.vo.ProductDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商城商品控制器
 * 负责处理商品搜索列表、商品详情查看等商城商品浏览功能
 */
@RestController
@RequestMapping("/shop/product")
@Tag(name = "商城-商品浏览")
public class ShopProductController {

    @Autowired
    private ProductService productService;  // 商品服务，处理商品相关业务逻辑

    @Autowired
    private ProductSkuMapper productSkuMapper;  // 商品SKU数据访问层，用于查询商品SKU信息

    /**
     * 搜索商品列表
     * 支持分页、关键字搜索、分类筛选、价格区间筛选
     * @param page 页码，默认1
     * @param size 每页大小，默认20
     * @param keyword 搜索关键字，支持商品名称模糊搜索
     * @param categoryId 分类ID，按分类筛选商品
     * @param minPrice 最低价格，按商品最低价格筛选
     * @param maxPrice 最高价格，按商品最高价格筛选
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
        // 创建分页参数对象
        Page<Product> pageParam = new Page<>(page, size);
        // 创建查询条件构建器
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        
        // 关键字搜索条件：模糊匹配商品名称
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Product::getName, keyword);
        }
        
        // 分类筛选条件：精确匹配分类ID
        if (categoryId != null) {
            wrapper.eq(Product::getCategoryId, categoryId);
        }
        
        // 最低价格筛选条件：大于等于指定价格
        if (minPrice != null) {
            wrapper.ge(Product::getMinPrice, minPrice);
        }
        
        // 最高价格筛选条件：小于等于指定价格
        if (maxPrice != null) {
            wrapper.le(Product::getMinPrice, maxPrice);
        }
        
        // 按创建时间倒序排序，最新商品在前
        wrapper.orderByDesc(Product::getCreateTime);
        
        // 执行分页查询
        return Result.success(productService.page(pageParam, wrapper));
    }

    /**
     * 获取商品详情
     * 包含商品基本信息和SKU列表
     * @param id 商品ID
     * @return 商品详情（包含SKU列表）
     */
    @GetMapping("/{id}")
    @Operation(summary = "商品详情")
    public Result<ProductDetailVO> detail(@PathVariable Long id) {
        // 根据商品ID查询商品信息
        Product product = productService.getById(id);
        if (product == null) {
            return Result.error("商品不存在");  // 商品不存在时返回错误
        }
        
        // 查询商品的SKU列表
        LambdaQueryWrapper<ProductSku> skuWrapper = new LambdaQueryWrapper<>();
        skuWrapper.eq(ProductSku::getProductId, id);  // 按商品ID筛选
        skuWrapper.eq(ProductSku::getDeleted, 0);  // 只查询未删除的SKU
        List<ProductSku> skus = productSkuMapper.selectList(skuWrapper);
        
        // 构建商品详情VO对象，包含商品基本信息和SKU列表
        ProductDetailVO vo = new ProductDetailVO();
        vo.setProduct(product);
        vo.setSkus(skus);
        
        return Result.success(vo);
    }
}