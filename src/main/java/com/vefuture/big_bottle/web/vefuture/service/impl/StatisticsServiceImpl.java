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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
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
    private IManageBiBottleService manageBiBottleService;
    @Autowired
    private PointStrategyContext pointStrategyContext;
    @Autowired
    private BVefutureBigBottleMapper bigBottleMapper;
    @Autowired
    private StatisticsMapper statisticsMapper;
    @Autowired
    private BlackListMapper blackListMapper;
    //总计积分
    @Value("${bigbottle.deliver.sum_token:10000}")
    private Integer sumToken = 1000;
    @Value("${bigbottle.deliver.receipt_token_limit:10}")
    private Integer receiptTokenLimit = 10;
    @Value("${bigbottle.deliver.wallet_token_limit:50}")
    private Integer walletTokenLimit = 50;

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
            List<B3tyTokenTransDto> b3tyTokenTransDTOList = getB3tyTokenList(dto);
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
            throw new BusinessException("导出失败!");
        }
    }

    //获取发币列表
    private List<B3tyTokenTransDto> getB3tyTokenList(StatisticsQueryDTO dto) {
        HttpServletRequest request = null;
        BigBottleQueryDTO bigBottleQueryDto = new BigBottleQueryDTO();
        bigBottleQueryDto.setCurrent(1);
        bigBottleQueryDto.setSize(Integer.MAX_VALUE);
        // todo
        bigBottleQueryDto.setIsTimeThreshold(false);
        bigBottleQueryDto.setRetinfoIsAvaild(true);
        bigBottleQueryDto.setStartDate(dto.getStartDate());
        bigBottleQueryDto.setEndDate(dto.getEndDate());
        bigBottleQueryDto.setIsDelete("0");

        Page<ManageBigBottleVo> page = new Page<>(bigBottleQueryDto.getCurrent(), bigBottleQueryDto.getSize());
        Page<ManageBigBottleVo> manageBigBottleVoList = manageBiBottleService.getBigBottleList(request, page, bigBottleQueryDto);
        List<ManageBigBottleVo> receiptList = manageBigBottleVoList.getRecords();

        LambdaQueryWrapper<BlackList> blackListLambdaQw = new LambdaQueryWrapper<>();
        blackListLambdaQw.in(BlackList::getBlackType, 1, 3);
        List<BlackList> blackLists = blackListMapper.selectList(blackListLambdaQw);
        log.info("===> 黑名单数量为:{}", blackLists.size());

        // 提取 list2 中的 id 到 Set，提升 contains 性能
        Set<String> walletBlackSet = blackLists.stream()
                .map(BlackList::getWalletAddress)
                .collect(Collectors.toSet());

        List<ManageBigBottleVo> fixedReceiptList = receiptList.stream()
                .filter(bottle -> !walletBlackSet.contains(bottle.getWalletAddress()))
                .collect(Collectors.toList());
        log.info("===>查询出所有的小票据条数:{}", receiptList.size());
        log.info("====> 适合的小票数量为:{}", fixedReceiptList.size());

        log.info("===> 其中一张小票为:{}", fixedReceiptList.get(fixedReceiptList.size() / 2));

        //超过20的置为20
        fixedReceiptList.parallelStream().forEach(receipt -> {
            receipt.setPreB3trToken(receipt.getReceiptPoint());
            if(receipt.getReceiptPoint() > receiptTokenLimit){
                receipt.setPreB3trToken(receiptTokenLimit);
            }
        });

        Integer totalToken = fixedReceiptList.stream()
                .mapToInt(ManageBigBottleVo::getPreB3trToken)
                .sum();
        log.info("===> 本次总Token为:{}", totalToken);

        //统计一下每个地址有几张小票
        Map<String, Integer> receiptNumMap = new HashMap<>();
        fixedReceiptList.stream().forEach(receipt -> {
            if(receiptNumMap.get(receipt.getWalletAddress()) == null){
                receiptNumMap.put(receipt.getWalletAddress(), 1);
            }else{
                receiptNumMap.put(receipt.getWalletAddress(), receiptNumMap.get(receipt.getWalletAddress()) +1 );
            }
        });
        int sumRec = receiptNumMap.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
        log.info("===> 验证总票数:{}, 应等于原总票数:{}", sumRec, fixedReceiptList.size());

        //统计一下每个地址的总积分
        Map<String, Integer> sumTokenMap = new HashMap<>();
        fixedReceiptList.stream().forEach(receipt -> {
            if(sumTokenMap.get(receipt.getWalletAddress()) == null){
                sumTokenMap.put(receipt.getWalletAddress(), receipt.getPreB3trToken());
            }else{
                sumTokenMap.put(receipt.getWalletAddress(), sumTokenMap.get(receipt.getWalletAddress()) + receipt.getPreB3trToken() );
            }
        });
        int sumToken = sumTokenMap.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
        log.info("===> 验证总积分:{}, 应等于原总积分:{}", sumToken, totalToken);

        //对每个map, 削去大于50的值
        for (Map.Entry<String, Integer> entry : sumTokenMap.entrySet()) {
            if (entry.getValue() > walletTokenLimit) {
                entry.setValue(walletTokenLimit);  // 直接修改原 Map
            }
        }

        //设定一个结果Map
        Map<String, Integer> resultMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : sumTokenMap.entrySet()) {
            resultMap.put(entry.getKey(), entry.getValue()/receiptNumMap.get(entry.getKey()));
        }
        //填入结果
        fixedReceiptList.forEach( receipt -> {
            receipt.setFinalB3trToken(resultMap.get(receipt.getWalletAddress()));
        });

        List<B3tyTokenTransDto> result = fixedReceiptList.stream()
                .map(vo -> {
                    B3tyTokenTransDto tokenTransDto = new B3tyTokenTransDto();
                    tokenTransDto.setWalletAddress(vo.getWalletAddress());
                    tokenTransDto.setImgUrl(vo.getImgUrl());
                    tokenTransDto.setB3tyToken(vo.getFinalB3trToken()); // 映射字段名不同
                    return tokenTransDto;
                })
                .collect(Collectors.toList());
        return result;
    }


}
