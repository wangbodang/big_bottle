package com.vefuture.big_bottle.web.vefuture.service.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.drew.imaging.ImageProcessingException;
import com.vefuture.big_bottle.common.util.BbDateTimeUtils;
import com.vefuture.big_bottle.common.util.ImageSourceDetector;
import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import com.vefuture.big_bottle.web.vefuture.mapper.BVefutureBigBottleMapper;
import com.vefuture.big_bottle.web.vefuture.strategy.deplast.DeplastStrategyContext;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.LlmStrategy;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.domain.RequestModel;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.domain.RetinfoBigBottle;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.domain.RetinfoDrink;
import com.vefuture.big_bottle.web.vefuture.strategy.points.PointStrategyContext;
import com.vefuture.big_bottle.web.websocket.WsSessionManager;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wangb
 * @date 2025/4/29
 * @description 异步处理图片识别的小票任务
 *
 */
@Slf4j
public class AsyncProcessReceiptTask implements Runnable{
    private WsSessionManager ws;
    private RequestModel requestModel;
    private BVefutureBigBottleMapper bigBottleMapper;
    private LlmStrategy llmStrategy;
    private DeplastStrategyContext deplastStrategyContext;
    private PointStrategyContext pointStrategyContext;

    public AsyncProcessReceiptTask(RequestModel requestModel,
                                   WsSessionManager manager,
                                   BVefutureBigBottleMapper bigBottleMapper,
                                   LlmStrategy llmStrategy,
                                   DeplastStrategyContext deplastStrategyContext,
                                   PointStrategyContext pointStrategyContext){
        this.requestModel = requestModel;
        this.ws = manager;
        this.bigBottleMapper = bigBottleMapper;
        this.llmStrategy = llmStrategy;
        this.deplastStrategyContext = deplastStrategyContext;
        this.pointStrategyContext = pointStrategyContext;
    }

    @Override
    public void run() {
        try {
            if (requestModel == null || ws == null || bigBottleMapper == null || llmStrategy == null || deplastStrategyContext == null) {
                log.error("AsyncProcessReceiptTask参数异常：requestModel={}, ws={}, bigBottleMapper={}, llmStrategy={}, deplastStrategyContext={}",
                        requestModel, ws, bigBottleMapper, llmStrategy, deplastStrategyContext);
                return;
            }
            log.info("-========++++++++++++++> 线程开始执行:{}", DateUtil.formatDateTime(new Date()));
            //log.info("-========++++++++++++++> 休眠20秒钟,再处理图片!");
            try {
                ws.sendToUser(requestModel.getWalletAddress(), "图片处理任务准备开始 code:200; time:"+ DateUtil.formatDateTime(new Date()));
                //Thread.sleep(20*1000);
                String walletAddress = requestModel.getWalletAddress();
                String imgUrl = requestModel.getImgUrl();
                String process_id = requestModel.getProcess_id();
                String llm = requestModel.getLlm();

                RetinfoBigBottle retinfoBigBottle = llmStrategy.call(requestModel);

                //以当前时间作为插入时间
                LocalDateTime currentTime = LocalDateTime.now();
                if(!retinfoBigBottle.getRetinfoIsAvaild()){
                    log.info("---> 该票据信息不完整");
                    //return ApiResponse.error(ResultCode.RECEIPT_ERR_UNAVAILABLE.getCode(), ResultCode.RECEIPT_ERR_UNAVAILABLE.getMessage());
                    //throw new BusinessException(ResultCode.RECEIPT_ERR_UNAVAILABLE.getCode(), ResultCode.RECEIPT_ERR_UNAVAILABLE.getMessage());
                    //存一个空信息到库里
                    saveNullReceiptToDb(requestModel, currentTime);
                    return;
                }

                //todo 存到数据库 此处处理ExifInfo及减塑量的问题
                ImageSourceDetector.DetectionResult detectionResult = new ImageSourceDetector.DetectionResult(ImageSourceDetector.ImageOrigin.UNKNOWN, ImageSourceDetector.DeviceType.UNKNOWN);
                try {
                    detectionResult = ImageSourceDetector.detect(imgUrl);
                } catch (ImageProcessingException | IOException e) {
                    log.error("===> 判定图片Exif信息异常:{}", e.getMessage());
                }
                saveToDb(requestModel, retinfoBigBottle, currentTime, detectionResult);

                //todo 此处消息分发到指定的前端
                //ws.sendToUser(requestModel.getWalletAddress(), "图片处理任务已完成 code:200; time:"+ DateUtil.formatDateTime(new Date()));
            }  catch (Exception e) {
                log.error("AsyncProcessReceiptTask执行异常", e);
            }
            log.info("-========++++++++++++++> 线程结束执行:{}", DateUtil.formatDateTime(new Date()));
        } catch (Exception e) {
            log.error("处理小票失败, walletAddress={}, imgUrl={}, 异常如下: ", requestModel.getWalletAddress(), requestModel.getImgUrl(), e);
            //throw new RuntimeException(e);
        } finally {
            requestModel = null;
            ws = null;
            bigBottleMapper = null;
            llmStrategy = null;
            deplastStrategyContext = null;
            pointStrategyContext = null;

        }
    }

    //空的时候也要一个流程ID
    //存储一个空的数据
    private void saveNullReceiptToDb(RequestModel requestModel, LocalDateTime currentTime) {
        BVefutureBigBottle bigBottle = new BVefutureBigBottle();
        bigBottle.setProcessId(requestModel.getProcess_id());
        bigBottle.setWalletAddress(requestModel.getWalletAddress());
        bigBottle.setImgUrl(requestModel.getImgUrl());
        bigBottle.setRetinfoIsAvaild(false);
        bigBottle.setIsDelete("1");
        bigBottle.setCreateTime(BbDateTimeUtils.localDateTimeToDate(currentTime));
        bigBottleMapper.insert(bigBottle);
    }

    //存储到数据库
    private void saveToDb(RequestModel requestModel, RetinfoBigBottle retinfoBigBottle, LocalDateTime currentTime, ImageSourceDetector.DetectionResult detectionResult) {

        ArrayList<RetinfoDrink> drinkList = retinfoBigBottle.getDrinkList();
        drinkList.forEach(drink -> {

            BVefutureBigBottle bigBottle = new BVefutureBigBottle();
            String walletAddressLowcase = requestModel.getWalletAddress().toLowerCase();
            String processId = requestModel.getProcess_id();
            String imgUrl = requestModel.getImgUrl();
            //公共信息
            bigBottle.setProcessId(processId);
            bigBottle.setWalletAddress(walletAddressLowcase);
            bigBottle.setImgUrl(imgUrl);
            bigBottle.setRetinfoIsAvaild(retinfoBigBottle.getRetinfoIsAvaild());
            bigBottle.setRetinfoReceiptTime(retinfoBigBottle.getRetinfoReceiptTime());
            bigBottle.setIsTimeThreshold(retinfoBigBottle.getTimeThreshold());
            //Exif信息
            bigBottle.setExifType(detectionResult.getOrigin().getCode());
            bigBottle.setExifDeviceType(detectionResult.getDeviceType().getCode());
            //饮料信息
            bigBottle.setRetinfoDrinkName(drink.getRetinfoDrinkName());
            bigBottle.setRetinfoDrinkCapacity(drink.getRetinfoDrinkCapacity());
            bigBottle.setRetinfoDrinkAmout(drink.getRetinfoDrinkAmout());
            //积分
            bigBottle.setDrinkPoint(pointStrategyContext.calculatePoints(bigBottle));
            //减塑量
            bigBottle.setDePlastic(deplastStrategyContext.caculDeplast(bigBottle));

            //根据该数据是否在数据库有记录判断是否为有效数据 isDelete = 1 为无效数据
            setDeletFlag(retinfoBigBottle.getRetinfoReceiptTime(), currentTime, drink, bigBottle, walletAddressLowcase);
            //一张小票用统一一个插入时间便于后期统计
            bigBottle.setCreateTime(BbDateTimeUtils.localDateTimeToDate(currentTime));
            bigBottleMapper.insert(bigBottle);
        });
    }

    //从数据库中判断该小票是否有重复的
    //todo 当前时间五秒前有相同数据, 判断为delete
    private void setDeletFlag(Date retinfoReceiptTime, LocalDateTime currentTime, RetinfoDrink drink, BVefutureBigBottle bigBottle, String walletAddress) {
        List<BVefutureBigBottle> bigBottleList;

        LambdaQueryWrapper<BVefutureBigBottle> queryWrapper = new LambdaQueryWrapper<>();
        //queryWrapper.eq(BVefutureBigBottle::getWalletAddress, walletAddress);
        queryWrapper.eq(BVefutureBigBottle::getRetinfoReceiptTime, retinfoReceiptTime);
        DateTime before5Sec = DateUtil.offsetSecond(BbDateTimeUtils.localDateTimeToDate(currentTime), -5);
        //时间跨度
        queryWrapper.le(BVefutureBigBottle::getCreateTime, before5Sec);
        queryWrapper.eq(BVefutureBigBottle::getIsDelete, "0");
        bigBottleList = bigBottleMapper.selectList(queryWrapper);
        if(CollectionUtil.isNotEmpty(bigBottleList)){
            bigBottle.setIsDelete("1");
        }
    }
}
