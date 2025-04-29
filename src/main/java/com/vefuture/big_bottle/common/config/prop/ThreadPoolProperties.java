package com.vefuture.big_bottle.common.config.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wangb
 * @date 2025/4/29
 * @description 线程池配置属性类
 */
@Data
@Component
@ConfigurationProperties(prefix = "task.pool")
public class ThreadPoolProperties {
    private int corePoolSize = 5;
    private int maxPoolSize = 10;
    private int queueCapacity = 1000;
    private int keepAliveSeconds = 300;
    // 省略getter/setter
}
