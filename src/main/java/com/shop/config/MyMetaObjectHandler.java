package com.shop.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis Plus元数据处理器
 * 用于自动填充实体类的公共字段，如创建时间、更新时间等
 */
@Component // 标记为Spring组件
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入操作时自动填充
     * @param metaObject 元数据对象，包含实体类的所有字段信息
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 严格填充创建时间字段
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        // 严格填充更新时间字段
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    /**
     * 更新操作时自动填充
     * @param metaObject 元数据对象，包含实体类的所有字段信息
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 严格填充更新时间字段
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}