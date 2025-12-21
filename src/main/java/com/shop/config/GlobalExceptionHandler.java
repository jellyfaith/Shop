package com.shop.config;

import com.shop.utils.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        e.printStackTrace();
        // 生产环境隐藏具体错误，只显示通用提示
        return Result.error("服务器繁忙，请稍后重试");
    }
    
    // 可以添加更多自定义异常处理
}
