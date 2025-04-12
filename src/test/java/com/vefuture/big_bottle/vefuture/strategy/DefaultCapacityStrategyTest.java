package com.vefuture.big_bottle.vefuture.strategy;

import com.vefuture.big_bottle.BigBottleApplication;
import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import com.vefuture.big_bottle.web.vefuture.strategy.points.DefaultCapacityStrategy;
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
public class DefaultCapacityStrategyTest {

    @Autowired
    private DefaultCapacityStrategy defaultStrategy;

    @Test
    public void testSupportsAndCalculate() {
        BVefutureBigBottle bottle = new BVefutureBigBottle();
        bottle.setRetinfoDrinkCapacity(1800);
        bottle.setRetinfoDrinkAmout(1);

        boolean supported = defaultStrategy.supports(bottle);
        int points = defaultStrategy.calculate(bottle);

        Assertions.assertTrue(supported);
        Assertions.assertEquals(10, points);
    }
}
