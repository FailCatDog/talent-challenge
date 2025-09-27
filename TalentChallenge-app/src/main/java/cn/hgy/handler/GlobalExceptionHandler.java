package cn.hgy.handler;

import cn.hgy.api.response.Response;
import cn.hgy.types.enums.ResponseCode;
import cn.hgy.types.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    // 处理自定义业务异常
    @ExceptionHandler(AppException.class)
    public Object handleBusinessException(AppException e) {
        // 1. 记录异常
        log.error("业务异常: {}", e.getMessage(), e);

        // 2. 返回错误信息
        return new Response<>(e.getCode(), e.getInfo(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(AppException e) {
        // 1. 记录异常
        log.error("业务异常: {}", e.getMessage(), e);

        // 2. 返回错误信息
        return new Response<>(e.getCode(), e.getInfo(), null);
    }

    // 处理其他未捕获异常
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return new Response<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getInfo(), null);
    }
}
