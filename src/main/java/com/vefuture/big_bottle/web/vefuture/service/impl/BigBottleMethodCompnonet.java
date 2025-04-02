package com.vefuture.big_bottle.web.vefuture.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vefuture.big_bottle.common.config.BigBottleProperties;
import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.common.enums.ResultCode;
import com.vefuture.big_bottle.common.exception.BusinessException;
import com.vefuture.big_bottle.common.util.BbDateTimeUtils;
import com.vefuture.big_bottle.common.util.OkHttpUtil;
import com.vefuture.big_bottle.common.vechain.BodyEntity;
import com.vefuture.big_bottle.common.vechain.ParameterEntity;
import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import com.vefuture.big_bottle.web.vefuture.entity.llm_ret.RetinfoBigBottle;
import com.vefuture.big_bottle.web.vefuture.entity.llm_ret.RetinfoDrink;
import com.vefuture.big_bottle.web.vefuture.entity.llm_ret.RetinfoLLMJson;
import com.vefuture.big_bottle.web.vefuture.mapper.BVefutureBigBottleMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangb
 * @date 2025/4/3
 * @description 方法类
 */
@Slf4j
@Component
public class BigBottleMethodCompnonet extends ServiceImpl<BVefutureBigBottleMapper, BVefutureBigBottle>{

    @Autowired
    private BigBottleProperties bigBottleProperties;
    //从单例class获取
    //private final OkHttpClient client = new OkHttpClient();
    private final OkHttpClient client = OkHttpUtil.getClient();

    //获取本周内小票列表
    public List<BVefutureBigBottle> getCurrWeekBigBottles(String walletAddress) {
        //当前本地时间
        LocalDateTime now = LocalDateTime.now();
        //限制插入时间为本周的开始
        Date currentTime = BbDateTimeUtils.localDateTimeToDate(now);
        DateTime beginOfWeek = DateUtil.beginOfWeek(BbDateTimeUtils.localDateTimeToDate(now));

        //获取本周的小票列表 todo 标志是否为空待确定
        List<BVefutureBigBottle> bigBottles = getBigBottleListByTimeOffset(walletAddress, beginOfWeek, currentTime, null, null);
        return bigBottles;
    }
    /**
     * 根据时间间隔选出相应的饮料列表
     *
     * @param
     * @return List 列表
     */
    public List<BVefutureBigBottle> getBigBottleListByTimeOffset(String walletAddress, Date startDateTime, Date endDateTime, Boolean retinfoIsAvaild, Boolean isTimeThreshold){
        List<BVefutureBigBottle> bigBottleList;

        LambdaQueryWrapper<BVefutureBigBottle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BVefutureBigBottle::getWalletAddress, walletAddress);
        //若两个标志为false, 则先把所有的数据都查出来
        if(ObjectUtil.isNotEmpty(retinfoIsAvaild)){
            queryWrapper.eq(BVefutureBigBottle::getRetinfoIsAvaild, retinfoIsAvaild);
        }
        if(ObjectUtil.isNotEmpty(isTimeThreshold)){
            queryWrapper.eq(BVefutureBigBottle::getIsTimeThreshold, isTimeThreshold);
        }
        //时间跨度
        queryWrapper.ge(BVefutureBigBottle::getCreateTime, startDateTime);
        queryWrapper.le(BVefutureBigBottle::getCreateTime, endDateTime);

        //按id排序
        queryWrapper.orderByDesc(BVefutureBigBottle::getId);

        bigBottleList = baseMapper.selectList(queryWrapper);

        return bigBottleList;
    }


    /**
     * 根据饮料信息返回积分
     * - 积分 = { 1, 如果 数量 < 1000 5, 如果 1000 ≤ 数量 ≤ 2000 7, 如果 数量 > 2000 }
     * @param
     * @return  返回值说明
     */

    public Integer getPointsByReceipts(List<BVefutureBigBottle> bigBottles) {
        Integer sumPoint = bigBottles.stream()
                .mapToInt(this::getPoints)
                .sum();
        return sumPoint;
    }

    /*
     * 如果两个都是true， 其他信息都有 但是饮料容积拿不到的时候 积分用第一条规则
     */
    private Integer getPoints(BVefutureBigBottle bigBottle) {
        Integer capacity = bigBottle.getRetinfoDrinkCapacity();
        if(ObjectUtil.isEmpty(capacity) && ObjectUtil.isNotEmpty(bigBottle.getRetinfoDrinkName()) && ObjectUtil.isNotEmpty(bigBottle.getRetinfoDrinkAmout())){
            return bigBottle.getRetinfoDrinkAmout() * (Integer) 1;
        }
        if(capacity < 1000)
            return bigBottle.getRetinfoDrinkAmout() * (Integer) 1;
        if(capacity <= 2000)
            return bigBottle.getRetinfoDrinkAmout() * (Integer) 5;
        if(capacity > 2000)
            return bigBottle.getRetinfoDrinkAmout() * (Integer) 7;
        return 0;
    }

    //查询出当前用户当天上传的小票张数
    public Integer getCurrDayCountByWalletAddress(String walletAddress) {
        //当前本地时间
        LocalDateTime now = LocalDateTime.now();
        //限制插入时间为当天
        Date currentTime = BbDateTimeUtils.localDateTimeToDate(now);
        DateTime beginOfDay = DateUtil.beginOfDay(currentTime);

        //获取当天的小票列 表
        List<BVefutureBigBottle> bigBottles = getBigBottleListByTimeOffset(walletAddress, beginOfDay, currentTime, null, null);
        if(CollectionUtil.isEmpty(bigBottles)) {
            return 0;
        }
        int count = bigBottles.stream()
                .collect(Collectors.toMap(
                        BVefutureBigBottle::getImgUrl,   // 去重 key（比如 name）
                        p -> p,            // 保留的值（这里是整个对象）
                        (existing, replacement) -> existing // 保留重复时的哪个（保留第一个）
                ))
                .size();  // 最后 map 的 size 就是去重后的 count
        return count;
    }

    public void sendReqAndSave(String walletAddress, String imgUrl) throws IOException {
        //构造请求参数
        ParameterEntity parameterEntity = new ParameterEntity();
        parameterEntity.setImg_url(imgUrl);
        BodyEntity bodyEntity = new BodyEntity();
        bodyEntity.setWorkflow_id(bigBottleProperties.getCoze_workflow_id());
        bodyEntity.setParameters(parameterEntity);

        // 3. 使用 Gson（或 Jackson 等）将实体转换为 JSON 字符串
        String jsonString = JSON.toJSONString(bodyEntity);
        log.info("---> 请求参数为:{}", jsonString);
        // 4. 将 JSON 字符串封装为 RequestBody
        RequestBody body = RequestBody.create(jsonString, MediaType.get("application/json; charset=utf-8"));


        Request request = new Request.Builder()
                .header("Authorization", bigBottleProperties.getCoze_token())
                .header("Content-Type", "application/json")
                .url(bigBottleProperties.getCoze_url())
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        String retJsonString = response.body().string();
        log.info("---> 返回值为:{}", retJsonString);

        RetinfoLLMJson retinfoLLMJson = JSON.parseObject(retJsonString, RetinfoLLMJson.class);
        String contentStr = retinfoLLMJson.getData();
        log.info("---> 返回的票据信息为:[{}]", contentStr);

        RetinfoBigBottle bigBottle = JSON.parseObject(contentStr, RetinfoBigBottle.class);

        if(!bigBottle.getRetinfoIsAvaild()){
            log.info("---> 该票据信息不完整");
            //return ApiResponse.error(ResultCode.RECEIPT_ERR_UNAVAILABLE.getCode(), ResultCode.RECEIPT_ERR_UNAVAILABLE.getMessage());
            throw new BusinessException(ResultCode.RECEIPT_ERR_UNAVAILABLE.getCode(), ResultCode.RECEIPT_ERR_UNAVAILABLE.getMessage());
        }

        //todo 存到数据库
        //以当前时间作为插入时间
        LocalDateTime currentTime = LocalDateTime.now();
        saveToDb(walletAddress, imgUrl, bigBottle, currentTime);
    }

    //存储到
    private void saveToDb(String walletAddress, String imgUrl, RetinfoBigBottle retinfoBigBottle, LocalDateTime currentTime) {

        ArrayList<RetinfoDrink> drinkList = retinfoBigBottle.getDrinkList();
        drinkList.forEach(drink -> {
            BVefutureBigBottle bigBottle = new BVefutureBigBottle();
            //公共信息
            bigBottle.setWalletAddress(walletAddress.toLowerCase());
            bigBottle.setImgUrl(imgUrl);
            bigBottle.setRetinfoIsAvaild(retinfoBigBottle.getRetinfoIsAvaild());
            bigBottle.setRetinfoReceiptTime(retinfoBigBottle.getRetinfoReceiptTime());
            bigBottle.setIsTimeThreshold(retinfoBigBottle.getTimeThreshold());
            //饮料信息
            bigBottle.setRetinfoDrinkName(drink.getRetinfoDrinkName());
            bigBottle.setRetinfoDrinkCapacity(drink.getRetinfoDrinkCapacity());
            bigBottle.setRetinfoDrinkAmout(drink.getRetinfoDrinkAmout());

            //一张小票用统一一个插入时间便于后期统计
            bigBottle.setCreateTime(BbDateTimeUtils.localDateTimeToDate(currentTime));
            this.save(bigBottle);
        });
    }
}
