package com.tool.common;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.vefuture.big_bottle.common.util.BbDateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author wangb
 * @date 2025/3/28
 * @description TODO: 类描述
 */
@Slf4j
public class DateTimeTest {

    @Test
    public void testLocalDateTime(){
        LocalDateTime now = LocalDateTime.now();
        // 指定时区，例如系统默认时区
        ZoneId zoneId = ZoneId.systemDefault();
        // 转换为 ZonedDateTime
        ZonedDateTime zonedDateTime = now.atZone(zoneId);
        // 转换为 Instant
        Instant instant = zonedDateTime.toInstant();
        // Instant 转为 Date
        Date date = Date.from(instant);

        log.info("LocalDateTime: " + now);
        log.info("Date: " + date);
    }

    @Test
    public void testHutool(){
        Date now = new Date();
        DateTime dateTime = DateUtil.beginOfWeek(now);
        log.info("---> dateTime:[{}]", DateUtil.formatDateTime(dateTime));

    }
    @Test
    public void testDateTimeUtil(){
        LocalDateTime now = LocalDateTime.now();
        DateTime dateTime = DateUtil.beginOfWeek(BbDateTimeUtils.localDateTimeToDate(now));
        log.info("---> {}", dateTime);

    }
}
