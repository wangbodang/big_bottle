package com.vefuture.big_bottle.web.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vefuture.big_bottle.web.system.entity.SysConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vefuture.big_bottle.web.system.entity.query.SysConfigQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.BlackList;
import com.vefuture.big_bottle.web.vefuture.entity.qo.BlackListQueryDTO;
import org.springframework.cache.annotation.Cacheable;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 参数配置表 服务类
 * </p>
 *
 * @author wangchao
 * @since 2025-05-19
 */
public interface SysConfigService extends IService<SysConfig> {

    Page<SysConfig> getSysConfigList(HttpServletRequest request, Page<SysConfig> page, SysConfigQueryDTO dto);

    @Cacheable(cacheNames = "config", key = "#configKey")
    String getConfigValue(String configKey);

    void updateConfig(HttpServletRequest request, SysConfigQueryDTO dto);
}
