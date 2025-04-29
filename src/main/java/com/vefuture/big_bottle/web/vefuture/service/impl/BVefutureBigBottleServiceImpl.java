package com.vefuture.big_bottle.web.vefuture.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vefuture.big_bottle.common.config.prop.BigBottleProperties;
import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.common.enums.ResultCode;
import com.vefuture.big_bottle.common.exception.BadRequestException;
import com.vefuture.big_bottle.common.exception.BusinessException;
import com.vefuture.big_bottle.common.util.UUIDCreator;
import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import com.vefuture.big_bottle.web.vefuture.entity.qo.ReqBigBottleQo;
import com.vefuture.big_bottle.web.vefuture.entity.vo.CardInfoVo;
import com.vefuture.big_bottle.web.vefuture.entity.vo.CountLimitVo;
import com.vefuture.big_bottle.web.vefuture.mapper.BVefutureBigBottleMapper;
import com.vefuture.big_bottle.web.vefuture.service.BVefutureBigBottleService;
import com.vefuture.big_bottle.web.vefuture.service.task.AsyncProcessTask;
import com.vefuture.big_bottle.web.websocket.WsSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

/**
 * <p>
 * vefuture bigbottle 基本信息表 服务实现类
 * </p>
 *
 * @author wangchao
 * @since 2025-03-15
 */
@Slf4j
@Service
@RequiredArgsConstructor        // Lombok 自动生成带 @Autowired 的构造器
public class BVefutureBigBottleServiceImpl extends ServiceImpl<BVefutureBigBottleMapper, BVefutureBigBottle> implements BVefutureBigBottleService {

    @Autowired
    private BigBottleProperties bigBottleProperties;
    @Autowired
    private BigBottleLogicProcessor bottleLogicProcessor;
    private final WsSessionManager ws;
    @Autowired
    private ExecutorService threadPoolExecutor;
    @Value("${bigbottle.counttimes.max:10}")
    private Integer countMax;
    @Value("${bigbottle.counttimes.lastnum:10}")
    private Integer lastNum;



    /**
     *
     * 显示的是最后一次上传的小票内容和本周的积分
     - icon 展示  状态
     - 右下角时间显示后端数据库记录的时间
     - 饮料名称（UI 做最大字符串的截断处理或多行设计）
     - 饮料容积，以 ml 作为单位
     - 饮料数量
     - 积分 = { 1, 如果 数量 < 1000 5, 如果 1000 ≤ 数量 ≤ 2000 7, 如果 数量 > 2000 }
     算出本周内的积分
     找出最后一次上传小票的时间

     失败：
     - icon 展示 false状态
     - 提示文字 “Your receipt doesn't meet the requirements”
     - 时间显示后端数据库记录的时间

     * @param  qo 包含钱包地址
     * @return  返回值说明
     */
    @Override
    public ApiResponse<CardInfoVo> getCardInfoByWalletAddress(ReqBigBottleQo qo) {
        //钱包地址
        String walletAddress = qo.getWalletAddress();
        if (StrUtil.isBlank(walletAddress)) {
            log.info("---> 缺失参数 walletAddress不能为空");
            throw new BadRequestException(ResultCode.RECEIPT_ERR_PARAMETER_NOT_COMPLETE.getCode(),
                    ResultCode.RECEIPT_ERR_PARAMETER_NOT_COMPLETE.getMessage());
        }
        walletAddress = walletAddress.toLowerCase();

        //获取本周内小票列表
        List<BVefutureBigBottle> currWeekBigBottles = bottleLogicProcessor.getCurrWeekBigBottles(walletAddress);

        /*if(CollectionUtil.isEmpty(currWeekBigBottles)){
            log.info("---> 钱包地址[{}]本周内没有合适的小票", walletAddress);
            return ApiResponse.error(ResultCode.RECEIPT_ERR_UNMEET.getCode(), ResultCode.RECEIPT_ERR_UNMEET.getMessage());
        }*/

        //获取最后几张饮料数据
        List<BVefutureBigBottle> lastBigBottles =  bottleLogicProcessor.getLastBigBottleList(walletAddress, lastNum);
        CardInfoVo cardInfoVo = new CardInfoVo();

        //把小票信息转换为card信息封装一下
        bottleLogicProcessor.setCardDrinkInfo(cardInfoVo, lastBigBottles);

        //设定本周有效小票的积分和
        Integer currWeekPoints = bottleLogicProcessor.getPointsByReceipts(currWeekBigBottles);
        cardInfoVo.setWeekPoints(currWeekPoints);

        ApiResponse<CardInfoVo> success = ApiResponse.success(cardInfoVo);
        return success;
    }

    /**
     * 根据钱包信息获取当周积分
     * @param qo
     * @return
     */
    @Override
    public ApiResponse<CardInfoVo> getWeekPointsByWalletAddress(ReqBigBottleQo qo) {

        CardInfoVo cardInfoVo = new CardInfoVo();
        //钱包地址和图片地址
        String walletAddress = qo.getWalletAddress();
        if (StrUtil.isBlank(walletAddress)) {
            log.info("---> 缺失参数 walletAddress不能为空");
            throw new BadRequestException(ResultCode.RECEIPT_ERR_PARAMETER_NOT_COMPLETE.getCode(),
                    ResultCode.RECEIPT_ERR_PARAMETER_NOT_COMPLETE.getMessage());
        }
        walletAddress = walletAddress.toLowerCase();

        //获取本周内小票列表
        List<BVefutureBigBottle> currentWeekBigBottles = bottleLogicProcessor.getCurrWeekBigBottles(walletAddress);
        if(CollectionUtil.isEmpty(currentWeekBigBottles)){
            log.info("---> 该地址:[{}]本周没有积分", walletAddress);
            cardInfoVo.setWeekPoints(0);
            return ApiResponse.success(cardInfoVo);
        }

        //获取本周积分
        Integer currWeekPoints = bottleLogicProcessor.getPointsByReceipts(currentWeekBigBottles);
        cardInfoVo.setWeekPoints(currWeekPoints);
        return ApiResponse.success(cardInfoVo);
    }

    /**
     * 根据用户的钱包确定当天上传的次数及每天最大次数信息
     * @param qo
     * @return
     */
    @Override
    public ApiResponse<CountLimitVo> getCountLimit(ReqBigBottleQo qo) {
        CountLimitVo countLimitVo = new CountLimitVo();
        //每天最大次数信息
        countLimitVo.setCountMax(countMax);
        //钱包地址和图片地址
        String walletAddress = qo.getWalletAddress();
        if (StrUtil.isBlank(walletAddress)) {
            log.info("---> 缺失参数 walletAddress不能为空");
            throw new BadRequestException(ResultCode.RECEIPT_ERR_PARAMETER_NOT_COMPLETE.getCode(),
                    ResultCode.RECEIPT_ERR_PARAMETER_NOT_COMPLETE.getMessage());
        }

        walletAddress = walletAddress.toLowerCase();
        //查询出当天上传的次数
        Integer currentCount = bottleLogicProcessor.getCurrDayCountByWalletAddress(walletAddress);
        countLimitVo.setCountCurrent(currentCount);
        return ApiResponse.success(countLimitVo);
    }

    /**
     * 此处生成UUID, 作为porcess_id用作全流程跟踪
     * @param  reqBigBottleQo
     * @return  返回值说明
     */
    @Override
    public ApiResponse processReceipt(ReqBigBottleQo reqBigBottleQo) {
        //此ID用作全流程跟踪ID
        String process_id = UUIDCreator.getUuidV7().toString();

        //钱包地址和图片地址
        String walletAddress = reqBigBottleQo.getWalletAddress();
        String imgUrl = reqBigBottleQo.getImgUrl();
        threadPoolExecutor.submit(new AsyncProcessTask(process_id, ws, walletAddress, imgUrl));
        if(StrUtil.isBlank(walletAddress) || StrUtil.isBlank(imgUrl)){
            log.info("---> 缺失参数 walletAddress imgUrl都不能为空");
            throw new BadRequestException(ResultCode.RECEIPT_ERR_PARAMETER_NOT_COMPLETE.getCode(),
                    ResultCode.RECEIPT_ERR_PARAMETER_NOT_COMPLETE.getMessage());
        }
        walletAddress = walletAddress.toLowerCase();

        //存入流程记录表
        

        Integer currDayCountByWalletAddress = bottleLogicProcessor.getCurrDayCountByWalletAddress(walletAddress);
        //判断今天上传次数是否达到最大次数
        if(currDayCountByWalletAddress >= countMax){
            log.info("---> 今天上传次数为:[{}], 已到达最大次数", currDayCountByWalletAddress);
            return ApiResponse.error(ResultCode.RECEIPT_MAX_SUBMIT_COUNT.getCode(), ResultCode.RECEIPT_MAX_SUBMIT_COUNT.getMessage());
        }
        try {
            //coze dify
            String llm = "dify";
            bottleLogicProcessor.sendReqAndSave(process_id, walletAddress, imgUrl, llm);
        } catch (BusinessException businessException){
            log.error("===> 业务异常:{}", businessException.getMessage());
            return ApiResponse.error(businessException.getCode(), businessException.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("===> 业务异常:{}", e.getMessage());
            return ApiResponse.error(ResultCode.INTERNAL_ERROR.getCode(), ResultCode.INTERNAL_ERROR.getMessage());
        }
        return ApiResponse.success();
    }


}
