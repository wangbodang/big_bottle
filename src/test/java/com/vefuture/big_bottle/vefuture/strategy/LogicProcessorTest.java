package com.vefuture.big_bottle.vefuture.strategy;

import com.vefuture.big_bottle.common.config.prop.BigBottleProperties;
import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import com.vefuture.big_bottle.web.vefuture.mapper.BVefutureBigBottleMapper;
import com.vefuture.big_bottle.web.vefuture.service.impl.BigBottleLogicProcessor;
import com.vefuture.big_bottle.web.vefuture.strategy.points.DefaultCapacityStrategy;
import com.vefuture.big_bottle.web.vefuture.strategy.points.FallbackSimpleStrategy;
import com.vefuture.big_bottle.web.vefuture.strategy.points.PointStrategyContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangb
 * @date 2025/4/12
 * @description TODO: 类描述
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BigBottleLogicProcessor.class,
        PointStrategyContext.class,
        DefaultCapacityStrategy.class,
        FallbackSimpleStrategy.class})
@ComponentScan(basePackages = "com.vefuture.big_bottle.web.vefuture.strategy.points")
public class LogicProcessorTest {

    @Autowired
    private PointStrategyContext pointStrategyContext;
    @Autowired
    private BigBottleLogicProcessor logicProcessor;
    @MockBean
    private BVefutureBigBottleMapper bVefutureBigBottleMapper;
    @MockBean
    private BigBottleProperties bigBottleProperties;


    @Test
    public void testPoints(){
        List<BVefutureBigBottle> bigBottleList = new ArrayList<>();
        BVefutureBigBottle bottle1 = new BVefutureBigBottle();
        bottle1.setRetinfoDrinkCapacity(1000);
        bottle1.setRetinfoDrinkAmout(2); // 1000ml * 10 分
        bigBottleList.add(bottle1);

        BVefutureBigBottle bottle2 = new BVefutureBigBottle();
        bottle2.setRetinfoDrinkCapacity(null);
        bottle2.setRetinfoDrinkAmout(2); // null * 1 分
        bigBottleList.add(bottle2);

        int bottle1Points = pointStrategyContext.calculatePoints(bottle1);
        int bottle2Points = pointStrategyContext.calculatePoints(bottle2);
        Integer pointsByReceipts = logicProcessor.getPointsByReceipts(bigBottleList);

        log.info("===> bottle1 获取的积分为:[{}]", bottle1Points);
        log.info("===> bottle2 获取的积分为:[{}]", bottle2Points);
        log.info("===> bigBottleList 获取的积分为:[{}]", pointsByReceipts);
        Assertions.assertEquals(20, bottle1Points);
        Assertions.assertEquals(2, bottle2Points);
        Assertions.assertEquals(22, pointsByReceipts);
    }

    @Test
    public void testUnsupportedStrategy() {
        BVefutureBigBottle bottle = new BVefutureBigBottle();
        bottle.setRetinfoDrinkCapacity(999999); // 假设所有策略都不支持的极端值

        /*
        Assertions.assertThrows(IllegalStateException.class, () -> {
            pointStrategyContext.calculatePoints(bottle);
        });
        */
        Assertions.assertEquals(15, pointStrategyContext.calculatePoints(bottle));
    }
}
