package com.vefuture.big_bottle.common.util;

import com.github.f4b6a3.uuid.UuidCreator;

import java.util.UUID;

/**
 * @author wangb
 * @date 2025/3/15
 * @description 生成UUID的工具类
 */
public class UUIDCreator {

    //UUID v1 (基于时间)
    public static UUID getUuidV1(){
        return UuidCreator.getTimeBased();
    }

    //UUID v4 (随机 UUID)
    public static UUID getUuidV4(){
        return UuidCreator.getRandomBased();
    }

    //UUID v6 (时间排序)
    public static UUID getUuidV6(){
        return UuidCreator.getTimeOrdered();
    }

    //UUID v7 (基于时间戳，适用于现代数据库)
    public static UUID getUuidV7(){
        return UuidCreator.getTimeOrderedEpoch();
    }
}
