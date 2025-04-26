package com.vefuture.big_bottle.web.vefuture.strategy.deplast;

import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wangb
 * @date 2025/4/24
 * @description TODO: 类描述
 */
@Component
public class DeplastStrategyContext {
    @Autowired
    private List<DeplastStrategy> strategies;

    public BigDecimal caculDeplast(BVefutureBigBottle bottle){
        return strategies.stream()
                .filter(strategy -> strategy.supports(bottle))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("没有合适的策略处理该 bottle"))
                .calculate(bottle);
    }
}
