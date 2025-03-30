package com.vefuture.big_bottle.web.vefuture.controller;

import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.common.enums.ResultCode;
import com.vefuture.big_bottle.common.exception.BusinessException;
import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import com.vefuture.big_bottle.web.vefuture.entity.qo.ReqBigBottleQo;
import com.vefuture.big_bottle.web.vefuture.entity.vo.CardInfoVo;
import com.vefuture.big_bottle.web.vefuture.service.BVefutureBigBottleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * vefuture bigbottle 基本信息表 前端控制器
 * </p>
 *
 * @author wangchao
 * @since 2025-03-15
 */
@Slf4j
@RestController
//@RequestMapping("/vefuture/bigbottle")
public class BVefutureBigBottleController {

    @Resource
    private BVefutureBigBottleService bigBottleService;


    /**
     *
     * 获取标签页的内容
     * @param  qo 请求参数
     * @return
     */
    @RequestMapping(value = "/cardinfo", method = RequestMethod.POST)
    public ApiResponse<CardInfoVo> getCardInfo(@RequestBody ReqBigBottleQo qo){
        log.info("---> 请求card_info时的钱包地址为:[{}]", qo.getWalletAddress());
        ApiResponse<CardInfoVo> cardInfo = bigBottleService.getCardInfoByWalletAddress(qo);
        return cardInfo;
    }

    /**
     *
     * 获取标签页的内容
     * @param  qo 请求参数
     * @return
     */
    @RequestMapping(value = "/weekpoints", method = RequestMethod.POST)
    public ApiResponse<CardInfoVo> getWeekPoints(@RequestBody ReqBigBottleQo qo){
        log.info("---> 请求card_info时的钱包地址为:[{}]", qo.getWalletAddress());
        ApiResponse<CardInfoVo> cardInfo = bigBottleService.getWeekPointsByWalletAddress(qo);
        return cardInfo;
    }
    /**
     * 前端发送
     *
     * @param  vo 前端VO
     * @return  返回值说明
     */
    @RequestMapping(value = "/process", method = RequestMethod.POST)
    public ApiResponse processReceipt(@RequestBody ReqBigBottleQo vo){
        log.info("---> 传过来的数据为:{}", vo);
        ApiResponse apiResponse = apiResponse = bigBottleService.processReceipt(vo);
        return apiResponse;
    }

    /**
     * 保存到数据库
     * @param bigBottle
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    //public RetResponse<BVefutureBigBottle> saveBigBottle(@RequestBody BVefutureBigBottle bigBottle){
    public ApiResponse<BVefutureBigBottle> saveBigBottle(BVefutureBigBottle bigBottle){
        log.info("---> 前端传过来的数据:{}", bigBottle);
        boolean save = false;
        try {
            save = bigBottleService.save(bigBottle);
            if (save) {
                log.info("---> 保存成功");
                ApiResponse<BVefutureBigBottle> retResponse = ApiResponse.success(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), bigBottle);
                return retResponse;
            }else{
                log.error("===> 保存失败");
                return ApiResponse.error(ResultCode.INTERNAL_ERROR.getCode(), ResultCode.INTERNAL_ERROR.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("===> 异常消息:{}", e.getMessage());
            throw new BusinessException(ResultCode.INTERNAL_ERROR.getCode(), ResultCode.INTERNAL_ERROR.getMessage(), e);
        }
    }

}
