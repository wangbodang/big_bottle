package com.vefuture.big_bottle.web.vefuture.controller;

import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.web.vefuture.entity.qo.BlackListQueryDTO;
import com.vefuture.big_bottle.web.vefuture.service.IBlackListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * vefuture 黑名单列表 前端控制器
 * </p>
 *
 * @author wangchao
 * @since 2025-05-05
 */
@Slf4j
@RestController
@RequestMapping("/manage/blacklist")
public class BlackListController {

    @Autowired
    private IBlackListService blackListService;

    @PostMapping("/remove")
    public ApiResponse<?> recoverBlackList(@RequestBody BlackListQueryDTO dto){
        log.info("===> 要恢复的地址ID为:{}", dto.getBlackId());
        blackListService.recoverBlack(dto);
        return ApiResponse.success();
    }
}
