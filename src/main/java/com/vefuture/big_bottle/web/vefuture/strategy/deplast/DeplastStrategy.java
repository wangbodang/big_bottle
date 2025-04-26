package com.vefuture.big_bottle.web.vefuture.strategy.deplast;

import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;

import java.math.BigDecimal;

/**
 * @author wangb
 * @date 2025/4/24
 * @description 关于减塑的策略接口
 */
public interface DeplastStrategy {
    boolean supports(BVefutureBigBottle bottle);
    BigDecimal calculate(BVefutureBigBottle bottle);
}
