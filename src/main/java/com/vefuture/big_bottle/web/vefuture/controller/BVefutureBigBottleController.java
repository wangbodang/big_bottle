package com.vefuture.big_bottle.web.vefuture.controller;


import com.vefuture.big_bottle.common.annotation.BlacklistCheck;
import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.common.enums.ResultCode;
import com.vefuture.big_bottle.common.exception.BusinessException;
import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import com.vefuture.big_bottle.web.vefuture.entity.qo.BigBottleQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.vo.CardInfoVo;
import com.vefuture.big_bottle.web.vefuture.entity.vo.CountLimitVo;
import com.vefuture.big_bottle.web.vefuture.entity.vo.InBlackListDto;
import com.vefuture.big_bottle.web.vefuture.service.BVefutureBigBottleService;
import com.vefuture.big_bottle.web.websocket.WsSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

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
@RequestMapping("/vefuture")
public class BVefutureBigBottleController {

    @Resource
    private BVefutureBigBottleService bigBottleService;

    private final WsSessionManager ws;
    public BVefutureBigBottleController(WsSessionManager ws) {
        this.ws = ws;
    }


    /**
     *
     * 获取标签页的内容
     * @param  qo 请求参数
     * @return
     */
    @RequestMapping(value = "/cardinfo", method = RequestMethod.POST)
    public ApiResponse<CardInfoVo> getCardInfo(@RequestBody BigBottleQueryDTO qo){
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
    public ApiResponse<CardInfoVo> getWeekPoints(@RequestBody BigBottleQueryDTO qo){
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
    @BlacklistCheck(params = {"wallet_address", "walletAddress"})
    public ApiResponse processReceipt(HttpServletRequest request, @RequestBody BigBottleQueryDTO vo){
        log.info("---> 传过来的数据为:{}", vo);
        ApiResponse apiResponse = apiResponse = bigBottleService.processReceipt(request, vo);
        return apiResponse;
    }

    /**
     * 前端发送
     *
     * @param  vo 前端VO
     * @return  返回值说明
     */
    @RequestMapping(value = "/inblacklist", method = RequestMethod.POST)
    //@BlacklistCheck(params = {"wallet_address", "walletAddress"})
    public ApiResponse<InBlackListDto> walletAddressInBalckList(@RequestBody BigBottleQueryDTO vo){
        log.info("---> 传过来的数据为:{}", vo);
        ApiResponse<InBlackListDto> apiResponse = apiResponse = bigBottleService.wallletInBlackList(vo);
        return apiResponse;
    }

    /**
     * 根据钱包地址获取该钱包每天上传次数的限制
     *
     * @param  qo
     * @return  返回值说明
     */
    @RequestMapping(value = "/countlimit", method = RequestMethod.POST)
    public ApiResponse<CountLimitVo> getCountLimit(@RequestBody BigBottleQueryDTO qo){
        ApiResponse<CountLimitVo> responseVo = bigBottleService.getCountLimit(qo);
        return responseVo;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ApiResponse getTestInfo(){
        log.info("---> 到达测试请求后台...................");
        try {
            Optional<BVefutureBigBottle> optById = bigBottleService.getOptById(1);
            log.info("---> 查询数据成功:{}", optById);
            // 推送给指定用户
            //ws.sendToUser("U42", "图片处理任务已完成 code:200; time:"+ DateUtil.formatDateTime(new Date()));
            return ApiResponse.success(optById);
        } catch (Exception e) {
            log.error("---> 查询数据异常:{}", e.getMessage());
            return ApiResponse.error(ResultCode.NOT_FOUND.getCode(), "未查询到数据");
        }
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
