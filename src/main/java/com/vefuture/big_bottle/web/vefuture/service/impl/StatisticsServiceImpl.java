package com.vefuture.big_bottle.web.vefuture.service.impl;

import cn.hutool.core.util.StrUtil;
import com.vefuture.big_bottle.web.vefuture.entity.qo.StatisticsQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.vo.StatisticsResultDTO;
import com.vefuture.big_bottle.web.vefuture.mapper.StatisticsMapper;
import com.vefuture.big_bottle.web.vefuture.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * @author wangb
 * @date 2025/5/17
 * @description T统计类接口ServiceImpl
 *
 */
@Slf4j
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Override
    public StatisticsResultDTO getStatisticsReult(StatisticsQueryDTO dto) {
        log.info("===>>> 查询参数为:{}", dto);
        //处理endDate:
        if (dto.getEndDate() != null) {
            dto.setEndDate(dto.getEndDate().plusSeconds(86399)); // 或 plusDays(1).minusSeconds(1)
        }
        StatisticsResultDTO resultDTO = new StatisticsResultDTO();

        //指定日期钱包总数
        Integer walletAddressCount = statisticsMapper.queryWalletAddressCount(dto);
        resultDTO.setWalletAddressCount(walletAddressCount);
        //指定日期图片总数
        Integer totalImageCount = statisticsMapper.getTotalImageCount(dto);
        resultDTO.setTotalImageCount(totalImageCount);
        //指定时间内通过的地址数
        Integer passedAddressCount = statisticsMapper.getPassedAddressCount(dto);
        resultDTO.setPassedAddressCount(passedAddressCount);
        //指定时间内通过的小票数
        Integer passedReceiptCount = statisticsMapper.getPassedReceiptCount(dto);
        resultDTO.setPassedReceiptCount(passedReceiptCount);
        //指定时间驳回的的地址数(无效):
        Integer unpassedAddressCount = statisticsMapper.getUnpassedAddressCount(dto);
        resultDTO.setUnpassedAddressCount(unpassedAddressCount);
        //指定时间内驳回的小票数(无效):
        Integer unpassedReceiptCount = statisticsMapper.getunpassedReceiptCount(dto);
        resultDTO.setUnpassedReceiptCount(unpassedReceiptCount);

        return resultDTO;
    }
}
