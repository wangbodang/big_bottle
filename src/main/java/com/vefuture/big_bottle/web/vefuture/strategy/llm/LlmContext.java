package com.vefuture.big_bottle.web.vefuture.strategy.llm;

import com.vefuture.big_bottle.web.vefuture.strategy.llm.domain.RequestModel;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.domain.RetinfoBigBottle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author wangb
 * @date 2025/4/26
 * @description 大模型调用时的策略上下文
 */
@Slf4j
@Component
public class LlmContext {
    private LlmStrategy strategy;

    //设置模型
    public void setStrategy(LlmStrategy strategy) {
        this.strategy = strategy;
    }

    //调用模型
    public RetinfoBigBottle execute(RequestModel RequestModel) {
        return strategy.call(RequestModel);
    }
}
