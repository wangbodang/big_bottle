package com.vefuture.big_bottle.web.auth.controller;


// UserController.java
import com.vefuture.big_bottle.common.annotation.RequireRole;
import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.common.enums.ResultCode;
import com.vefuture.big_bottle.web.auth.entity.LoginUser;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api")
public class TestController {

    @GetMapping("/userinfo")
    public Object getUserInfo(HttpServletRequest request) {
        LoginUser user = (LoginUser) request.getAttribute("loginUser");

        if (user == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", 401);
            map.put("msg", "未登录");
            return map;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "success");
        map.put("data", user);
        return map;
    }


    @RequireRole("admin")
    @GetMapping("/admin_only")
    public ApiResponse testRolesAuth(HttpServletRequest request){
        LoginUser user = (LoginUser) request.getAttribute("loginUser");
        return ApiResponse.success(ResultCode.SUCCESS.getCode(), "欢迎管理员", user);
    }
}
