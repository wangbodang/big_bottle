package com.vefuture.big_bottle.web.vefuture.strategy.deplast;

import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author wangb
 * @date 2025/4/24
 * @description TODO: 类描述
 */
@Component
public class NormalDepastStragy implements DeplastStrategy{
    @Override
    public boolean supports(BVefutureBigBottle bottle) {
        return bottle.getRetinfoDrinkCapacity() != null;
    }

    @Override
    public BigDecimal calculate(BVefutureBigBottle bottle) {
        Integer capacity = bottle.getRetinfoDrinkCapacity();

        if(capacity <= 300) return BigDecimal.valueOf(0);

        Integer amount = bottle.getRetinfoDrinkAmout() == null ? 1 : bottle.getRetinfoDrinkAmout();

        return PlasticSaving.delta(BigDecimal.valueOf(capacity)).multiply(BigDecimal.valueOf(amount)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }


}
