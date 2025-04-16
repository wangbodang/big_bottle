package com.vefuture.big_bottle.web.auth.controller;


// UserController.java
import com.vefuture.big_bottle.common.annotation.RequireRole;
import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.common.enums.ResultCode;
import com.vefuture.big_bottle.web.auth.entity.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangb
 * @date 2025/3/30
 * @description TODO: 类描述
 */
@Slf4j
@RestController
public class TestController {

    @GetMapping("/public/hello")
    public String helloPublic() {
        log.info("---> 公共请求到达 /public/hello");
        return "Hello Public";
    }

    //@GetMapping("/admin/hello")
    public String helloAdmin() {
        log.info("---> 私用请求到达 /admin/hello");
        return "Hello Admin";
    }

    //验证权限
    @GetMapping("/user/list")
    @PreAuthorize("hasAuthority('sys:user:view')")
    public ApiResponse<String> getUserList() {
        log.info("---> 验证用户是否有资源:[sys:user:view]");
        return ApiResponse.success("用户列表数据（模拟）");
    }
}
