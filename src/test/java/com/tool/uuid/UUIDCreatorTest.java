package com.tool.uuid;

import com.github.f4b6a3.uuid.UuidCreator;
import com.vefuture.big_bottle.common.util.UUIDCreator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.UUID;

/**
 * @author wangb
 * @date 2025/3/15
 * @description 测试UUID
 */
@Slf4j
public class UUIDCreatorTest {

    @Test
    public void testUUID(){
        System.out.println("UUID v1 (基于时间): " + UuidCreator.getTimeBased());
        System.out.println("UUID v4 (随机 UUID): " + UuidCreator.getRandomBased());
        System.out.println("UUID v6 (时间排序): " + UuidCreator.getTimeOrdered());
        System.out.println("UUID v7 (基于时间戳，适用于现代数据库): " + UuidCreator.getTimeOrderedEpoch());
    }

    @Test
    public void testUUIDCreator(){
        UUID uuidV7 = UUIDCreator.getUuidV7();
        log.info("---> 随机V7的UUID:{}", uuidV7.toString());
        //log.info("---> 随机V7的UUID:{}", uuidV7.timestamp());
    }

    @Test
    public void testBigInt(){
        String hexStr = "c402cfaf48f7a145b9c95242601e6bb26162fe8d"; // 去掉 "0x"
        log.info("长度:{}", hexStr.length());
        // 使用 BigInteger 存储
        BigInteger bigInt = new BigInteger(hexStr, 16);

        // 输出十进制
        System.out.println("十进制表示: " + bigInt);

        // 转回 16 进制
        System.out.println("十六进制表示: 0x" + bigInt.toString(16));
    }
}
