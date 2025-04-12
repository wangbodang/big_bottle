package com.vefuture.big_bottle.vefuture.strategy;

import com.vefuture.big_bottle.BigBottleApplication;
import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import com.vefuture.big_bottle.web.vefuture.service.impl.BigBottleLogicProcessor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangb
 * @date 2025/4/12
 * @description TODO: 类描述
 */
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(classes = BigBottleApplication.class)
@ComponentScan(basePackages = "com.vefuture.big_bottle")
public class BigBottleLogicProcessorTest {

    @Autowired
    private BigBottleLogicProcessor logicProcessor;

    @Test
    public void testCaculatePoints(){
        List<BVefutureBigBottle> bigBottleList = new ArrayList<>();
        BVefutureBigBottle bottle1 = new BVefutureBigBottle();
        bottle1.setRetinfoDrinkCapacity(1000);
        bottle1.setRetinfoDrinkAmout(2); // 1000ml * 10 分
        bigBottleList.add(bottle1);

        BVefutureBigBottle bottle2 = new BVefutureBigBottle();
        bottle2.setRetinfoDrinkCapacity(null);
        bottle2.setRetinfoDrinkAmout(2); // null * 1 分
        bigBottleList.add(bottle2);

        Integer pointsByReceipts = logicProcessor.getPointsByReceipts(bigBottleList);

        log.info("===> 获取的积分为:[{}]", pointsByReceipts);
        Assertions.assertEquals(22, pointsByReceipts);

    }
}
