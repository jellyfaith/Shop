package com.shop.vo;

import com.shop.entity.Product;
import com.shop.entity.ProductSku;
import lombok.Data;

import java.util.List;

/**
 * 商品详情视图对象
 * 用于封装商品的完整详细信息，包含商品基本信息和所有可用的SKU（库存单元）列表
 * 通常用于商品详情页，展示商品的完整信息和可选规格
 */
@Data  // Lombok注解，自动生成getter、setter、toString、equals、hashCode等方法
public class ProductDetailVO {
    /**
     * 商品基本信息对象
     * 包含商品的通用信息，如名称、描述、主图、品牌等
     */
    private Product product;
    
    /**
     * 商品SKU列表
     * 包含该商品所有可用的规格和库存信息
     * 每个SKU代表商品的一个具体规格（如颜色、尺寸、价格、库存等）
     */
    private List<ProductSku> skus;
}