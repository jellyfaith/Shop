package com.shop.config;

import com.shop.utils.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 * 用于统一处理所有Controller层抛出的异常，返回统一的响应格式
 */
@RestControllerAdvice // 标记为全局异常处理器，处理所有@RestController的异常
public class GlobalExceptionHandler {

    /**
     * 处理所有Exception类型的异常
     * @param e 异常对象
     * @return Result对象，包含错误信息
     */
    @ExceptionHandler(Exception.class) // 处理Exception类型的异常
    public Result<String> handleException(Exception e) {
        // 打印异常堆栈信息，方便调试
        e.printStackTrace();
        // 生产环境隐藏具体错误，只返回通用提示信息
        return Result.error("服务器繁忙，请稍后重试");
    }
    
    // 可以添加更多自定义异常处理方法，如处理特定类型的异常
    // 例如：@ExceptionHandler(MyException.class)
}