package com.vefuture.big_bottle.vefuture.strategy;

import com.vefuture.big_bottle.BigBottleApplication;
import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import com.vefuture.big_bottle.web.vefuture.strategy.points.PointStrategyContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wangb
 * @date 2025/4/12
 * @description TODO: 类描述
 */
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(classes = BigBottleApplication.class)
@ComponentScan(basePackages = "com.vefuture.big_bottle")
public class PointStrategyContextTest {

    @Autowired
    private PointStrategyContext pointStrategyContext;

    @Test
    public void testCalculatePoints() {
        BVefutureBigBottle bottle = new BVefutureBigBottle();
        bottle.setRetinfoDrinkCapacity(1000);
        bottle.setRetinfoDrinkAmout(2); // 1000ml * 10 分

        int points = pointStrategyContext.calculatePoints(bottle);
        log.info("===> 测试的点数:[{}]", points);
        Assertions.assertEquals(20, points);
    }
}
