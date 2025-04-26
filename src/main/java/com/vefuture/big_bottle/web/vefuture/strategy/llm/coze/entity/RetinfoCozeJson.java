package com.vefuture.big_bottle.web.vefuture.strategy.llm.coze.entity;

import lombok.Data;

/**
 * @author wangb
 * @date 2025/3/15
 * @description TODO: 类描述
 */
@Data
public class RetinfoCozeJson {
    private Integer code;
    private String cost;
    //返回的饮料消息字符串
    private String data;
    private String debug_url;
    private String  msg;
    private Integer token;
}
