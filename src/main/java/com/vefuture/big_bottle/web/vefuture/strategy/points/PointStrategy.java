package com.vefuture.big_bottle.web.vefuture.strategy.points;

import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;

/**
 * @author wangb
 * @date 2025/4/12
 * @description 积分策略
 */
public interface PointStrategy {
    boolean supports(BVefutureBigBottle bottle);
    int calculate(BVefutureBigBottle bottle);
}
