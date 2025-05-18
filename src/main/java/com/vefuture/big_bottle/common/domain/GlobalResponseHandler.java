package com.vefuture.big_bottle.common.domain;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
/**
 * @author wangb
 * @date 2025/5/17
 * @description TODO: 类描述
 */
//@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // true 表示对所有Controller返回值生效，你也可以加条件，比如只处理 @RestController 标注的类
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        // 如果已经是 ApiResponse，就不重复封装（避免重复包裹）
        if (body instanceof ApiResponse) {
            return body;
        }

        // 统一封装为 ApiResponse
        return ApiResponse.success(body);
    }
}

