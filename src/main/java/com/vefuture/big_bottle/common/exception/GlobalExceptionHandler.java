package com.vefuture.big_bottle.common.exception;


import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.common.domain.RetResponse;
import com.vefuture.big_bottle.common.enums.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BlacklistException.class)
    public ApiResponse handleBlacklistException(BlacklistException ex) {
        return ApiResponse.error(ResultCode.RECEIPT_IN_BLACK_LIST.getCode(), ResultCode.RECEIPT_IN_BLACK_LIST.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDenied(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.forbidden(ResultCode.FORBIDDEN.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAuth(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.unauthorized(ResultCode.UNAUTHORIZED.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ApiResponse<?> handleBadRequest(BadRequestException ex) {
        return ApiResponse.error(ex.getCode(), ex.getMessage());
    }

    // 捕获所有自定义业务异常
    @ExceptionHandler(BusinessException.class)
    public ApiResponse handleBusinessException(BusinessException ex) {
        ApiResponse error = ApiResponse.error(ex.getCode(), ex.getMessage());
        return error;
    }

    // 处理权限异常
    @ExceptionHandler(ForbiddenException.class)
    public ApiResponse handleForbidden(ForbiddenException ex) {
        return ApiResponse.error(ResultCode.FORBIDDEN.getCode(), ex.getMessage());
    }

    // 捕获所有其他异常
    @ExceptionHandler(Exception.class)
    public ApiResponse handleException(Exception ex) {
        // 可以记录日志
        log.error("***********===>>>未处理异常：", ex);
        ApiResponse error = ApiResponse.error(ResultCode.INTERNAL_ERROR.getCode(), ResultCode.INTERNAL_ERROR.getMessage());
        return error;
    }
}
