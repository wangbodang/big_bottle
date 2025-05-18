package com.vefuture.big_bottle.web.vefuture.service;

import com.vefuture.big_bottle.web.vefuture.entity.qo.StatisticsQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.vo.StatisticsResultDTO;

/**
 * @author wangb
 * @date 2025/5/17
 * @description 统计类接口Service
 */
public interface StatisticsService {
    StatisticsResultDTO getStatisticsReult(StatisticsQueryDTO dto);
}
