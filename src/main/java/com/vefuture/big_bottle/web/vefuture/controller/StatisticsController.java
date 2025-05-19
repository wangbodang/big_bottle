package com.vefuture.big_bottle.web.vefuture.controller;

import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.common.enums.ResultCode;
import com.vefuture.big_bottle.web.vefuture.entity.qo.StatisticsQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.vo.StatisticsResultDTO;
import com.vefuture.big_bottle.web.vefuture.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangb
 * @date 2025/5/17
 * @description 统计Controller
 */
@Slf4j
@RestController
@RequestMapping("/manage")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @PostMapping("/statistics")
    public ApiResponse<StatisticsResultDTO> statisticsMethod(@RequestBody StatisticsQueryDTO dto){
        StatisticsResultDTO resultDTO = null;
        try {
            resultDTO = statisticsService.getStatisticsReult(dto);
        } catch (Exception e) {
            log.error("====> 统计出错:{}", e.getMessage(), e);
            return ApiResponse.error(ResultCode.INTERNAL_ERROR.getCode(), ResultCode.INTERNAL_ERROR.getMessage());
        }
        return ApiResponse.success(resultDTO);
    }

}
