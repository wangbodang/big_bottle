package com.vefuture.big_bottle.web.vefuture.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vefuture.big_bottle.common.util.BbDateTimeUtils;
import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import com.vefuture.big_bottle.web.vefuture.mapper.BVefutureBigBottleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
}
