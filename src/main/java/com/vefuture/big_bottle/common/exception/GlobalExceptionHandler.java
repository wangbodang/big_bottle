package com.vefuture.big_bottle.common.exception;


import com.vefuture.big_bottle.common.domain.RetResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕获所有自定义业务异常
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<RetResponse> handleBusinessException(BusinessException ex) {
        RetResponse error = new RetResponse(400, ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 捕获所有其他异常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RetResponse> handleException(Exception ex) {
        // 可以记录日志
        RetResponse error = new RetResponse(500, "系统内部错误，请联系管理员！");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
