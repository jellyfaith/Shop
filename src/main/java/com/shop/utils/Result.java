package com.shop.utils;

import lombok.Data;

/**
 * 统一响应结果类
 * 用于封装API接口的响应数据，包含状态码、消息和响应数据
 * @param <T> 响应数据的泛型类型
 */
@Data
public class Result<T> {
    /**
     * 响应状态码
     * 200表示成功，其他值表示失败
     */
    private Integer code;

    /**
     * 响应消息
     * 描述响应的结果信息
     */
    private String message;

    /**
     * 响应数据
     * 包含API返回的具体数据
     */
    private T data;

    /**
     * 创建成功的响应结果（无数据）
     *
     * @param <T> 响应数据的泛型类型
     * @return 成功的响应结果对象
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 创建成功的响应结果（带数据）
     *
     * @param data 响应数据
     * @param <T>  响应数据的泛型类型
     * @return 成功的响应结果对象
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200); // 设置成功状态码
        result.setMessage("Success"); // 设置成功消息
        result.setData(data); // 设置响应数据
        return result;
    }

    /**
     * 创建错误的响应结果（带自定义状态码和消息）
     *
     * @param code    错误状态码
     * @param message 错误消息
     * @param <T>     响应数据的泛型类型
     * @return 错误的响应结果对象
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code); // 设置错误状态码
        result.setMessage(message); // 设置错误消息
        return result;
    }

    /**
     * 创建错误的响应结果（默认状态码500）
     *
     * @param message 错误消息
     * @param <T>     响应数据的泛型类型
     * @return 错误的响应结果对象
     */
    public static <T> Result<T> error(String message) {
        return error(500, message); // 使用默认错误状态码500
    }
}