package com.vefuture.big_bottle.web.vefuture.service;

import com.vefuture.big_bottle.web.vefuture.entity.qo.StatisticsQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.vo.StatisticsResultDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @author wangb
 * @date 2025/5/17
 * @description 统计类接口Service
 */
public interface StatisticsService {
    StatisticsResultDTO getStatisticsReult(StatisticsQueryDTO dto);
    //导出CSV
    void exportCsv(HttpServletRequest request, HttpServletResponse response, StatisticsQueryDTO dto);

    void reCalPoint();

    void exportCsvStream(OutputStream outputStream, StatisticsQueryDTO dto);

    StatisticsResultDTO caculateToken(HttpServletRequest request, StatisticsQueryDTO dto);
}
