package com.vefuture.big_bottle.common.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author wangb
 * @date 2025/3/29
 * @description 全局下划线命名改为驼峰命名
 */
@Configuration
public class JacksonConfig {
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> builder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        // 不用时间戳
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//        // 旧版 java.util.Date 全局格式
//        objectMapper.setDateFormat(new SimpleDateFormat(DATETIME_FORMAT));
//
//        // Java 8 Time API 配置
//        JavaTimeModule javaTimeModule = new JavaTimeModule();
//
//        // LocalDateTime
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
//        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
//
//        // LocalDate
//        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
//        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
//
//        // OffsetDateTime
//        javaTimeModule.addSerializer(OffsetDateTime.class, new JsonSerializer<OffsetDateTime>() {
//            @Override
//            public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//                gen.writeString(value.format(dateTimeFormatter)); // 同样使用 yyyy-MM-dd HH:mm:ss
//            }
//        });
//        objectMapper.registerModule(javaTimeModule);
//        return objectMapper;
//    }

}