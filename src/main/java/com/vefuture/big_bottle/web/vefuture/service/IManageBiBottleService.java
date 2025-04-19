package com.vefuture.big_bottle.web.vefuture.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vefuture.big_bottle.web.vefuture.entity.qo.ReqBigBottleQo;
import com.vefuture.big_bottle.web.vefuture.entity.vo.ManageBigBottleVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author wangb
 * @date 2025/4/19
 * @description TODO: 类描述
 */
public interface IManageBiBottleService {
    Page<ManageBigBottleVo> getBigBottleList(HttpServletRequest request, Page<ManageBigBottleVo> page, ReqBigBottleQo qo);
}
