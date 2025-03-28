package com.vefuture.big_bottle.common.util;
import java.time.*;
import java.util.Date;

/**
 * @author wangb
 * @date 2025/3/28
 * @description TODO: 类描述
 */
public class BbDateTimeUtils {
    private static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault();

    // Date -> LocalDateTime
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atZone(DEFAULT_ZONE).toLocalDateTime();
    }

    // LocalDateTime -> Date
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(DEFAULT_ZONE).toInstant());
    }

    // Date -> LocalDate
    public static LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(DEFAULT_ZONE).toLocalDate();
    }

    // LocalDate -> Date
    public static Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(DEFAULT_ZONE).toInstant());
    }

    // 自定义时区：LocalDateTime -> Date
    public static Date localDateTimeToDate(LocalDateTime localDateTime, ZoneId zoneId) {
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    // 自定义时区：Date -> LocalDateTime
    public static LocalDateTime dateToLocalDateTime(Date date, ZoneId zoneId) {
        return date.toInstant().atZone(zoneId).toLocalDateTime();
    }

    // 获取当前时间的 LocalDateTime
    public static LocalDateTime nowLocalDateTime() {
        return LocalDateTime.now();
    }

    // 获取当前时间的 Date
    public static Date nowDate() {
        return new Date();
    }

    // LocalDateTime -> ZonedDateTime（带时区）
    public static ZonedDateTime localDateTimeToZonedDateTime(LocalDateTime localDateTime) {
        return localDateTime.atZone(DEFAULT_ZONE);
    }

    // LocalDateTime -> OffsetDateTime（带偏移量）
    public static OffsetDateTime localDateTimeToOffsetDateTime(LocalDateTime localDateTime) {
        return localDateTime.atOffset(ZoneOffset.ofTotalSeconds(DEFAULT_ZONE.getRules().getOffset(Instant.now()).getTotalSeconds()));
    }

    // LocalDateTime -> Instant（精确时间戳）
    public static Instant localDateTimeToInstant(LocalDateTime localDateTime) {
        return localDateTime.atZone(DEFAULT_ZONE).toInstant();
    }

    // Instant -> LocalDateTime（从精确时间戳转回）
    public static LocalDateTime instantToLocalDateTime(Instant instant) {
        return instant.atZone(DEFAULT_ZONE).toLocalDateTime();
    }

}
