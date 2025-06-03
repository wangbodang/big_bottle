package com.vefuture.big_bottle.web.vefuture.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vefuture.big_bottle.common.exception.BusinessException;
import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import com.vefuture.big_bottle.web.vefuture.entity.BlackList;
import com.vefuture.big_bottle.web.vefuture.entity.qo.BigBottleQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.qo.StatisticsQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.vo.B3tyTokenTransDto;
import com.vefuture.big_bottle.web.vefuture.entity.vo.ManageBigBottleVo;
import com.vefuture.big_bottle.web.vefuture.entity.vo.StatisticsResultDTO;
import com.vefuture.big_bottle.web.vefuture.mapper.BVefutureBigBottleMapper;
import com.vefuture.big_bottle.web.vefuture.mapper.BlackListMapper;
import com.vefuture.big_bottle.web.vefuture.mapper.StatisticsMapper;
import com.vefuture.big_bottle.web.vefuture.service.IManageBiBottleService;
import com.vefuture.big_bottle.web.vefuture.service.StatisticsService;
import com.vefuture.big_bottle.web.vefuture.strategy.points.PointStrategyContext;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

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
    private PointStrategyContext pointStrategyContext;
    @Autowired
    private BVefutureBigBottleMapper bigBottleMapper;
    @Autowired
    private StatisticsMapper statisticsMapper;
    @Autowired
    private StatisticsLogicProcessor statisticsLogicProcessor;

    /**
     * 统计页面首页
     * @param dto
     * @return
     */
    @Override
    public StatisticsResultDTO getStatisticsReult(StatisticsQueryDTO dto) {
        log.info("获取统计信息时查询参数为:{}", dto);
        //处理endDate:
        if (dto.getEndDate() != null) {
            dto.setEndDate(dto.getEndDate().plusSeconds(86399)); // 或 plusDays(1).minusSeconds(1)
        }
        //获取统计信息
        StatisticsResultDTO resultDTO = statisticsLogicProcessor.getStatisticsResultDTO(dto);
        return resultDTO;
    }

    /**
     * 重新计算积分和代币的关系
     * @param request
     * @param dto
     * @return
     */
    @Override
    public StatisticsResultDTO caculateToken(HttpServletRequest request, StatisticsQueryDTO dto) {
        log.info("重新计算积分和代币值时的参数为:{}", dto);
        //处理endDate:
        if (dto.getEndDate() != null) {
            dto.setEndDate(dto.getEndDate().plusSeconds(86399)); // 或 plusDays(1).minusSeconds(1)
        }
        //获取统计信息
        StatisticsResultDTO resultDTO = statisticsLogicProcessor.getStatisticsResultDTO(dto);
        return resultDTO;
    }

    //重算积分 只有为空才重算
    @Override
    public void reCalPoint() {
        //找出drinkPoint为null的值
        LambdaQueryWrapper<BVefutureBigBottle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.isNull(BVefutureBigBottle::getDrinkPoint);
        List<BVefutureBigBottle> allBottle = bigBottleMapper.selectList(queryWrapper);
        allBottle.stream().forEach(bottle ->{
            if(bottle.getDrinkPoint() == null){
                bottle.setDrinkPoint(pointStrategyContext.calculatePoints(bottle));
                bigBottleMapper.updateById(bottle);
            }
        });
    }

    @Override
    public void exportCsv(HttpServletRequest request, HttpServletResponse response, StatisticsQueryDTO dto) {
        //处理endDate:

        String nowStr = DateUtil.formatDateTime(new Date()).replaceAll(" ", "_").replaceAll(":", "_");
        String fileName = "B3ty_Token_" + nowStr + ".csv";

        try {
            // 设置响应头
            response.setContentType("text/csv");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

            // 获取输出流
            ServletOutputStream outputStream = response.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
            BufferedWriter writer = new BufferedWriter(osw);

            // 写入 BOM 头（防止 Excel 中文乱码）
            writer.write('\ufeff');

            // 写入 CSV 表头
            writer.write("address,amount,reason");
            writer.newLine();

            // 查询数据（你应该根据 dto 查询出数据）
            // 替换为你实际的方法
            List<B3tyTokenTransDto> b3tyTokenTransDTOList = statisticsLogicProcessor.getB3tyTokenList(dto);
            // 写入数据行
            for (B3tyTokenTransDto item : b3tyTokenTransDTOList) {
                writer.write(String.format("%s,%s,%s",
                        item.getWalletAddress(),
                        item.getB3tyToken(),
                        item.getImgUrl()
                ));
                writer.newLine();
            }

            writer.flush();
            writer.close();

        } catch (IOException e) {
            log.error("===>>>>>>>导出失败", e.getMessage());
            throw new BusinessException("导出失败!" + e.getMessage());
        }
    }
    public void exportCsvStream(OutputStream outputStream, StatisticsQueryDTO dto) {
        try (OutputStreamWriter osw = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(osw)) {

            writer.write('\ufeff'); // BOM 防乱码
            writer.write("address,amount,reason");
            writer.newLine();

            //获取发币列表
            List<B3tyTokenTransDto> list = statisticsLogicProcessor.getB3tyTokenList(dto);

            for (B3tyTokenTransDto item : list) {
                writer.write(String.format("%s,%s,%s",
                        item.getWalletAddress(),
                        item.getB3tyToken(),
                        item.getImgUrl()));
                writer.newLine();
                writer.flush(); // 建议每写一行就 flush 一下，避免积压
            }
        } catch (IOException e) {
            log.warn("[exportCsvStream]导出过程中发生异常", e);
        }
    }



}
