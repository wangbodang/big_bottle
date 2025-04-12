package com.vefuture.big_bottle.web.vefuture.strategy.points;

import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import org.springframework.stereotype.Component;

/**
 * @author wangb
 * @date 2025/4/12
 * @description TODO: 类描述
 */
@Component
public class FallbackSimpleStrategy implements PointStrategy{

    public boolean supports(BVefutureBigBottle bottle) {
        return bottle.getRetinfoDrinkCapacity() == null;
    }
    public int calculate(BVefutureBigBottle bottle) {
        int amount = bottle.getRetinfoDrinkAmout() == null ? 1 : bottle.getRetinfoDrinkAmout();
        return amount * 1;
    }
}
