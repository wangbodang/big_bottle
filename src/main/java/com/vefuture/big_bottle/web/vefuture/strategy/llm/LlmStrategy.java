package com.vefuture.big_bottle.web.vefuture.strategy.llm;

import com.vefuture.big_bottle.web.vefuture.strategy.llm.domain.RequestModel;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.domain.RetinfoBigBottle;

/**
 * @author wangb
 * @date 2025/4/26
 * @description 大模型调用时的策略接口
 */
public interface LlmStrategy {
    RetinfoBigBottle call(RequestModel requestModel);
}
