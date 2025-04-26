package com.vefuture.big_bottle.web.vefuture.strategy.llm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangb
 * @date 2025/4/26
 * @description 大模型调用时的策略请求体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestModel {
    //钱包地址
    private String walletAddress;
    //图片URL
    private String imgUrl;
    //
    private String process_id;
    //
    private String llm;
}
