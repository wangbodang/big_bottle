package com.vefuture.big_bottle.web.vefuture.service;

import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vefuture.big_bottle.web.vefuture.entity.ReqBigBottleVo;

/**
 * <p>
 * vefuture bigbottle 基本信息表 服务类
 * </p>
 *
 * @author wangchao
 * @since 2025-03-15
 */
public interface BVefutureBigBottleService extends IService<BVefutureBigBottle> {

    /**
     *
     *
     * @param  vo 前端数据
     * @return  返回值说明
     */

    ApiResponse processReceipt(ReqBigBottleVo vo);
}
