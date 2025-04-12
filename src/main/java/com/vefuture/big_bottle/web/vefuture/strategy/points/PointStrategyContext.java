package com.vefuture.big_bottle.web.vefuture.strategy.points;

import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author wangb
 * @date 2025/4/12
 * @description 策略上下文
 */
@Slf4j
@Component
public class PointStrategyContext {

    @Autowired
    private List<PointStrategy> strategies;

    @PostConstruct
    public void printStrategies() {
        log.info("已注入的策略数量: [{}]", strategies.size());
        strategies.forEach(s -> log.info("策略类: [{}]",s.getClass().getName()));
    }

    public int calculatePoints(BVefutureBigBottle bottle) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(bottle))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("没有合适的策略处理该 bottle"))
                .calculate(bottle);
    }

    /*
    public int calculatePoints(BVefutureBigBottle bottle) {
        for (PointStrategy strategy : strategies) {
            if (strategy.supports(bottle)) {
                return strategy.calculate(bottle);
            }
        }
        throw new IllegalStateException("没有匹配的积分策略");
    }
    */
}
