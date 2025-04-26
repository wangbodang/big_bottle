package com.vefuture.big_bottle.web.vefuture.strategy.deplast;

import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author wangb
 * @date 2025/4/24
 * @description TODO: 类描述
 */
@Component
public class FallbackDeplastStrategy implements DeplastStrategy{
    @Override
    public boolean supports(BVefutureBigBottle bottle) {
        return bottle.getRetinfoDrinkCapacity() == null;
    }

    @Override
    public BigDecimal calculate(BVefutureBigBottle bottle) {
        return new BigDecimal("0");
    }
}
