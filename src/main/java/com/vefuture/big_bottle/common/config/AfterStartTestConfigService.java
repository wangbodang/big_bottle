package com.vefuture.big_bottle.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.awt.*;

/**
 * @author wangb
 * @date 2025/3/26
 * @description TODO: 类描述
 */
@Slf4j
@Component
public class AfterStartTestConfigService implements CommandLineRunner {
    @Autowired
    private BigBottleProperties bigBottleProperties;
    @Override
    public void run(String... args) throws Exception {
        log.info("---> 测试启动后执行...");
        log.info("---> 参数测试 url:[{}]workflow_id:[{}],token:[{}]", bigBottleProperties.getCoze_url(), bigBottleProperties.getCoze_workflow_id(), bigBottleProperties.getCoze_token());

    }
}
