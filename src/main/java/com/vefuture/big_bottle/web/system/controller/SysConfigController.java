package com.vefuture.big_bottle.web.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.common.enums.ResultCode;
import com.vefuture.big_bottle.web.system.entity.SysConfig;
import com.vefuture.big_bottle.web.system.entity.query.SysConfigQueryDTO;
import com.vefuture.big_bottle.web.system.service.SysConfigService;
import com.vefuture.big_bottle.web.vefuture.entity.BlackList;
import com.vefuture.big_bottle.web.vefuture.entity.qo.BlackListQueryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 参数配置表 前端控制器
 * </p>
 *
 * @author wangchao
 * @since 2025-05-19
 */
@Slf4j
@RestController
@RequestMapping("/manage/sysConfig")
public class SysConfigController {

    @Autowired
    private SysConfigService sysConfigService;
    /**
     *
     * @return
     */
    @GetMapping("/test")
    public ApiResponse<?> testDemo(){
        log.info("--->>> 测试");
        return ApiResponse.success();
    }
    /**
     * 获取配置列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ApiResponse<Page<SysConfig>> getConfigPage(HttpServletRequest request,
                                                      HttpServletResponse response,
                                                      @RequestBody SysConfigQueryDTO dto){
        log.info("---> 查询配置列表:page dto:[{}]", dto);
        Page<SysConfig> page = new Page<>(dto.getCurrent(), dto.getSize());

        Page<SysConfig> blackList = sysConfigService.getSysConfigList(request, page, dto);
        return ApiResponse.success(blackList);
    }

    /**
     * 修改配置
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ApiResponse updateConfig(HttpServletRequest request,
                                                      HttpServletResponse response,
                                                      @RequestBody SysConfigQueryDTO dto){
        sysConfigService.updateConfig(request, dto);
        return ApiResponse.success();
    }

}
