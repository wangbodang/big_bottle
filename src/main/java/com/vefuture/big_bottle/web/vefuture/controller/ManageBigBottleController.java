package com.vefuture.big_bottle.web.vefuture.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.web.vefuture.entity.qo.ReqBigBottleQo;
import com.vefuture.big_bottle.web.vefuture.entity.vo.ManageBigBottleVo;
import com.vefuture.big_bottle.web.vefuture.service.IManageBiBottleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author wangb
 * @date 2025/4/19
 * @description 管理饮料列表Controller
 */
@Slf4j
@RestController
@RequestMapping("/manage")
public class ManageBigBottleController {

    @Autowired
    private IManageBiBottleService manageBiBottleService;

    /**
     * 获取饮料列表
     * @return
     */
    @RequestMapping(value = "/bigbottlelist", method = RequestMethod.POST)
    public ApiResponse<Page<ManageBigBottleVo>> getBigBottleList(HttpServletRequest request,
                                           HttpServletResponse response,
                                           Page<ManageBigBottleVo> page, @RequestBody ReqBigBottleQo qo){
        log.info("---> 查询管理BigBottle列表:[{}]", qo);
        Page<ManageBigBottleVo> manageBigBottleVoList = manageBiBottleService.getBigBottleList(request, page, qo);

        return ApiResponse.success(manageBigBottleVoList);
    }
}
