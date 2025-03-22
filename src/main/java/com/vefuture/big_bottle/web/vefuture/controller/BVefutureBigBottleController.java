package com.vefuture.big_bottle.web.vefuture.controller;

import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.common.domain.RetResponse;
import com.vefuture.big_bottle.common.enums.ResultCode;
import com.vefuture.big_bottle.common.exception.BusinessException;
import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import com.vefuture.big_bottle.web.vefuture.entity.ReqBigBottleVo;
import com.vefuture.big_bottle.web.vefuture.service.BVefutureBigBottleService;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/vefuture/bigbottle")
public class BVefutureBigBottleController {

    @Resource
    private BVefutureBigBottleService bigBottleService;

    /**
     * 保存到数据库
     * @param bigBottle
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    //public RetResponse<BVefutureBigBottle> saveBigBottle(@RequestBody BVefutureBigBottle bigBottle){
    public RetResponse<BVefutureBigBottle> saveBigBottle(BVefutureBigBottle bigBottle){
        log.info("---> 前端传过来的数据:{}", bigBottle);
        boolean save = false;
        try {
            save = bigBottleService.save(bigBottle);
            if (save) {
                log.info("---> 保存成功");
                RetResponse<BVefutureBigBottle> retResponse = new RetResponse<>(1, "保存成功", bigBottle);
                return retResponse;
            }else{
                log.error("===> 保存失败");
                return new RetResponse<>(ResultCode.INTERNAL_ERROR.getCode(), ResultCode.INTERNAL_ERROR.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("===> 异常消息:{}", e.getMessage());
            throw new BusinessException(ResultCode.INTERNAL_ERROR.getCode(), ResultCode.INTERNAL_ERROR.getMessage(), e);
        }
    }

    /**
     *
     *
     * @param  vo 前端VO
     * @return  返回值说明
     */
    @RequestMapping(value = "/receipt", method = RequestMethod.POST)
    public ApiResponse processReceipt(ReqBigBottleVo vo){
        log.info("---> 传过来的数据为:{}", vo);
        ApiResponse apiResponse = apiResponse = bigBottleService.processReceipt(vo);
        return apiResponse;
    }
}
