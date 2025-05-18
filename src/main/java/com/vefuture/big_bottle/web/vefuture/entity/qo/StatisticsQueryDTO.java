package com.vefuture.big_bottle.web.vefuture.entity.qo;

import lombok.Data;

import java.time.OffsetDateTime;

/**
 * @author wangb
 * @date 2025/5/17
 * @description TODO: 类描述
 */
@Data
public class StatisticsQueryDTO {
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
}
