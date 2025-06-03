package com.vefuture.big_bottle.web.vefuture.controller;

import cn.hutool.core.date.DateUtil;
import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.common.enums.ResultCode;
import com.vefuture.big_bottle.web.vefuture.entity.qo.StatisticsQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.vo.StatisticsResultDTO;
import com.vefuture.big_bottle.web.vefuture.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

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

    /**
     * 这个方法是重新计算那些没有积分的小票信息
     * @return
     */
    @RequestMapping("/recalpoint")
    public ApiResponse reCalPoint(){
        log.info("===> 重新计算积分:");
        statisticsService.reCalPoint();
        return ApiResponse.success();
    }

    /**
     * 计算本次积分和代币
     * @return
     */
    @PostMapping("/statistics/calculate")
    public ApiResponse<StatisticsResultDTO> calculateToken(HttpServletRequest request, HttpServletResponse response, @RequestBody StatisticsQueryDTO dto){
        StatisticsResultDTO resultDTO = statisticsService.caculateToken(request, dto);
        return ApiResponse.success(resultDTO);
    }

    //导出
    @PostMapping("/statistics/export")
    public ResponseEntity<StreamingResponseBody> export(HttpServletRequest request, HttpServletResponse response, @RequestBody StatisticsQueryDTO dto){
        log.info("===> 导出的请求参数为:{}", dto);
        statisticsService.reCalPoint();

        String nowStr = DateUtil.formatDateTime(new Date()).replaceAll(" ", "_").replaceAll(":", "_");
        String fileName = "B3ty_Token_" + nowStr + ".csv";

        StreamingResponseBody stream = outputStream -> statisticsService.exportCsvStream(outputStream, dto);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(stream);
    }
}
