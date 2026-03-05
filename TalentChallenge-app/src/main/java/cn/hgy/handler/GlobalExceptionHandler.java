package cn.hgy.handler;

import cn.hgy.api.response.Response;
import cn.hgy.types.enums.ResponseCode;
import cn.hgy.types.exception.AppException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public Object handleBusinessException(AppException e) {
        log.error("业务异常: {}", e.getMessage(), e);
        return new Response<>(e.getCode(), e.getInfo(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(AppException e) {
        log.error("业务异常: {}", e.getMessage(), e);
        return new Response<>(e.getCode(), e.getInfo(), null);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public Object handleExpiredJwtException(ExpiredJwtException e) {
        log.warn("JWT令牌已过期: {}", e.getMessage());
        return new Response<>(ResponseCode.TOKEN_EXPIRED.getCode(), ResponseCode.TOKEN_EXPIRED.getInfo(), null);
    }

    @ExceptionHandler(JwtException.class)
    public Object handleJwtException(JwtException e) {
        log.warn("JWT令牌无效: {}", e.getMessage());
        return new Response<>(ResponseCode.TOKEN_INVALID.getCode(), ResponseCode.TOKEN_INVALID.getInfo(), null);
    }

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return new Response<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getInfo(), null);
    }
}

