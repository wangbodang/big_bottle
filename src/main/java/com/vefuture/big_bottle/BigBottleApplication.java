package com.vefuture.big_bottle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@Slf4j
@SpringBootApplication
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BigBottleApplication {

    public static void main(String[] args) {
        SpringApplication.run(BigBottleApplication.class, args);
        log.info("---> Big Bottle 启动成功");
    }

}
