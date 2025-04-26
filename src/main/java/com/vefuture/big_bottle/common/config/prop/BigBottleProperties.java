package com.vefuture.big_bottle.common.config.prop;

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
@ConfigurationProperties(prefix = "bigbottle")
public class BigBottleProperties {
    private CozeProperties coze;
    private DifyProperties dify;

    @Data
    public static class CozeProperties {
        private String coze_url;
        private String coze_workflow_id;
        private String coze_token;
    }

    @Data
    public static class DifyProperties {
        private String dify_app_key;
        private String dify_url;
        // 以后如果要加其他 dify 的配置，比如
        // private String api_url;
        // private String project_id;
        // 直接加字段就行了！
    }
}
