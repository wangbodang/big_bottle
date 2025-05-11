package com.vefuture.big_bottle.web.vefuture.service;

import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vefuture.big_bottle.web.vefuture.entity.qo.BigBottleQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.vo.CardInfoVo;
import com.vefuture.big_bottle.web.vefuture.entity.vo.CountLimitVo;
import com.vefuture.big_bottle.web.vefuture.entity.vo.InBlackListDto;

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

    ApiResponse processReceipt(BigBottleQueryDTO vo);

    /**
     *
     - icon 展示  状态
     - 右下角时间显示后端数据库记录的时间
     - 饮料名称（UI 做最大字符串的截断处理或多行设计）
     - 饮料容积，以 ml 作为单位
     - 饮料数量
     - 积分 = {
                 1, 如果 数量 < 1000
                 5, 如果 1000 ≤ 数量 ≤ 2000
                 7, 如果 数量 > 2000
             }
     * @param  qo 包含钱包地址
     * @return  返回值说明
     */
    ApiResponse<CardInfoVo> getCardInfoByWalletAddress(BigBottleQueryDTO qo);

    /**
     *
     * 获取周积分
     * @param  qo
     * @return  返回值说明
     */

    ApiResponse<CardInfoVo> getWeekPointsByWalletAddress(BigBottleQueryDTO qo);

    /**
     * 根据用户的钱包确定当天上传的次数是否到达最大的限制
     * @param qo
     * @return
     */
    ApiResponse<CountLimitVo> getCountLimit(BigBottleQueryDTO qo);

    ApiResponse<InBlackListDto> wallletInBlackList(BigBottleQueryDTO vo);
}
