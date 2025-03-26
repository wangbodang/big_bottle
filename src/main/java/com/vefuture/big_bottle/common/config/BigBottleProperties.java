package com.vefuture.big_bottle.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangb
 * @date 2025/3/26
 * @description TODO: 类描述
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "bigbottle.coze")
public class BigBottleProperties {
    private String coze_url;
    private String coze_workflow_id;
    private String coze_token;
}
