package com.vefuture.big_bottle.web.vefuture.strategy.llm.coze.entity;

import lombok.Data;

/**
 * @author wangb
 * @date 2025/12/3
 * @description TODO: 类描述
 */
@Data
public class ImageContent {
    String type = "image";
    String transfer_method = "remote_url";
    String url;
}
