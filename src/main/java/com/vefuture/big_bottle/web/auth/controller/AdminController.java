package com.vefuture.big_bottle.web.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangb
 * @date 2025/4/16
 * @description TODO: 类描述
 */
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @PostMapping("/hello")
    @PreAuthorize("hasRole('ADMIN')")
    public String helloAdmin() {
        log.info("---> 私用请求到达 /admin/hello");
        return "Hello Admin";
    }
}
