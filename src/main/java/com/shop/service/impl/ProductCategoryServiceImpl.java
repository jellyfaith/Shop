package com.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.entity.ProductCategory;
import com.shop.repository.ProductCategoryMapper;
import com.shop.service.ProductCategoryService;
import org.springframework.stereotype.Service;

/**
 * 商品分类服务实现类
 * 
 * 该类是商品分类服务的具体实现，继承自MyBatis Plus提供的ServiceImpl类，
 * 实现了ProductCategoryService接口。通过继承ServiceImpl，该类自动获得了
 * 大量的CRUD（增删改查）操作方法，无需手动实现这些基础功能。
 * 
 * MyBatis Plus的ServiceImpl是一个通用服务实现类，它接受两个泛型参数：
 * 
 *   ProductCategoryMapper：数据访问层接口，用于执行数据库操作
 *   ProductCategory：实体类，对应数据库中的商品分类表
 * 
 * 通过这种方式，我们可以快速构建一个功能完整的服务层实现，
 * 只需关注业务逻辑的实现，而无需编写重复的基础CRUD代码。
 */
@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {
    // 该类继承了ServiceImpl的所有方法，无需额外实现基础CRUD操作
    // 如果需要扩展业务逻辑，可以在此处添加自定义方法
}