package com.vefuture.big_bottle.web.vefuture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vefuture.big_bottle.web.system.service.SysConfigService;
import com.vefuture.big_bottle.web.vefuture.entity.BlackList;
import com.vefuture.big_bottle.web.vefuture.entity.qo.BigBottleQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.qo.StatisticsQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.vo.B3tyTokenTransDto;
import com.vefuture.big_bottle.web.vefuture.entity.vo.ManageBigBottleVo;
import com.vefuture.big_bottle.web.vefuture.entity.vo.StatisticsResultDTO;
import com.vefuture.big_bottle.web.vefuture.mapper.BlackListMapper;
import com.vefuture.big_bottle.web.vefuture.mapper.StatisticsMapper;
import com.vefuture.big_bottle.web.vefuture.service.IManageBiBottleService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wangb
 * @date 2025/6/3
 * @description 统计逻辑处理
 */
@Slf4j
@Component
public class StatisticsLogicProcessor {

    @Autowired
    private IManageBiBottleService manageBiBottleService;
    @Autowired
    private StatisticsMapper statisticsMapper;
    @Autowired
    private BlackListMapper blackListMapper;
    @Autowired
    private SysConfigService sysConfigService;
    //总计积分
    /*@Value("${bigbottle.deliver.sum_token:10000}")
    private Integer sumToken = 1000;*/
    /*@Value("${bigbottle.deliver.receipt_token_limit:10}")
    private Integer receiptPointLimit = 10;*/
    /*@Value("${bigbottle.deliver.wallet_token_limit:50}")
    private Integer walletTokenLimit = 50;*/
    /**
     * 获取统计结果
     * @param dto
     * @return
     */
    @NotNull
    public StatisticsResultDTO getStatisticsResultDTO(StatisticsQueryDTO dto) {
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

        //取出系数跟总积分
        resultDTO.setConversionFactor(new BigDecimal(sysConfigService.getConfigValue("conversion_factor")));
        //处理endDate:
        if (dto.getEndDate() != null) {
            dto.setEndDate(dto.getEndDate().minusSeconds(86399)); // 或 plusDays(1).minusSeconds(1)
        }
        //计算总积分
        resultDTO.setSumToken(getSumTokenByList(getB3tyTokenList(dto)));
        return resultDTO;
    }
    //计算总积分
    private BigDecimal getSumTokenByList(List<B3tyTokenTransDto> b3tyTokenList) {
        log.info("===> 列表长度为:{}", b3tyTokenList.size());
        log.info("===> 列表的第一个数据为:{}", b3tyTokenList.get(0));
        log.info("===> 列表的最后一个数据为:{}", b3tyTokenList.get(b3tyTokenList.size()-1));
        return b3tyTokenList.stream().map(B3tyTokenTransDto::getB3tyToken).reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    //获取发币列表
    public List<B3tyTokenTransDto> getB3tyTokenList(StatisticsQueryDTO dto) {
        //用StopWatch计算各个查询用的时间
        StopWatch stopWatch = new StopWatch();
        //查询出小票列表
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

        stopWatch.start("Search1");
        Page<ManageBigBottleVo> manageBigBottleVoList = manageBiBottleService.getBigBottleList(request, page, bigBottleQueryDto);
        stopWatch.stop();

        List<ManageBigBottleVo> receiptList = manageBigBottleVoList.getRecords();

        LambdaQueryWrapper<BlackList> blackListLambdaQw = new LambdaQueryWrapper<>();
        blackListLambdaQw.in(BlackList::getBlackType, 1, 3);

        stopWatch.start("Search2");
        List<BlackList> blackLists = blackListMapper.selectList(blackListLambdaQw);
        stopWatch.stop();

        log.info("===> 黑名单数量为:{}", blackLists.size());

        stopWatch.start("Search3");
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

        //从数据库中找出相关的几个设置值
        int receiptPointLimit = Integer.parseInt(sysConfigService.getConfigValue("receipt_point_limit") );
        //比率
        BigDecimal conversionFactor = new BigDecimal(sysConfigService.getConfigValue("conversion_factor"));

        //超过20的置为20 此处用 BigDecimal
        fixedReceiptList.parallelStream().forEach(receipt -> {
            receipt.setPreB3trToken(BigDecimal.valueOf(receipt.getReceiptPoint()));
            if(receipt.getReceiptPoint() > receiptPointLimit){
                receipt.setPreB3trToken(BigDecimal.valueOf(receiptPointLimit));
            }
        });

        //用BigDecimal求和
        BigDecimal totalToken = fixedReceiptList.stream()
                .map(ManageBigBottleVo::getPreB3trToken)  // 假设返回 BigDecimal
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("===> 本次总Token为:{}", totalToken);

        //统计一下每个地址有几张小票
        Map<String, Integer> receiptNumMap = new HashMap<>(2000);
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
        Map<String, BigDecimal> sumTokenMap = new HashMap<>(2000);
        fixedReceiptList.stream().forEach(receipt -> {
            if(sumTokenMap.get(receipt.getWalletAddress()) == null){
                sumTokenMap.put(receipt.getWalletAddress(), receipt.getPreB3trToken());
            }else{
                //求和
                sumTokenMap.put(receipt.getWalletAddress(), sumTokenMap.get(receipt.getWalletAddress()).add(receipt.getPreB3trToken()));
            }
        });
        BigDecimal sumToken = sumTokenMap.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("===> 验证总积分:{}, 应等于原总积分:{}", sumToken, totalToken);

        //对每个map, 削去大于50的值 todo

        /*
        for (Map.Entry<String, Integer> entry : sumTokenMap.entrySet()) {
            if (entry.getValue() > walletTokenLimit) {
                entry.setValue(walletTokenLimit);  // 直接修改原 Map
            }
        }
        */

        //设定一个结果Map 里边只有地址和token数
        Map<String, BigDecimal> resultMap = new HashMap<>(2000);
        for (Map.Entry<String, BigDecimal> entry : sumTokenMap.entrySet()) {
            //用总值除以数量
            resultMap.put(entry.getKey(), entry.getValue().divide(BigDecimal.valueOf(receiptNumMap.get(entry.getKey())), BigDecimal.ROUND_HALF_UP));
        }
        //填入结果
        fixedReceiptList.forEach( receipt -> {
            //填入最后的数据 这里乘以系数
            BigDecimal tempFinalTokey = resultMap.get(receipt.getWalletAddress());
            BigDecimal multiplied = conversionFactor.multiply(tempFinalTokey).setScale(2, RoundingMode.HALF_UP);
            receipt.setFinalB3trToken(multiplied);
        });
        //获取CSV的数据列表
        List<B3tyTokenTransDto> result = fixedReceiptList.stream()
                .map(vo -> {
                    B3tyTokenTransDto tokenTransDto = new B3tyTokenTransDto();

                    tokenTransDto.setWalletAddress(vo.getWalletAddress());
                    tokenTransDto.setImgUrl(vo.getImgUrl());
                    tokenTransDto.setB3tyToken(vo.getFinalB3trToken()); // 映射字段名不同
                    return tokenTransDto;
                })
                .collect(Collectors.toList());
        stopWatch.stop();

        // 最终打印
        printStopWatchInSeconds(stopWatch, log);
        return result;
    }

    public static void printStopWatchInSeconds(StopWatch stopWatch, Logger tLog) {
        for (StopWatch.TaskInfo task : stopWatch.getTaskInfo()) {
            long millis = task.getTimeMillis();
            String secondsFormatted = String.format("%.3f", millis / 1000.0);
            tLog.info("任务 [{}] 耗时: {} ms(约 {} 秒)", task.getTaskName(), millis, secondsFormatted);
        }
        String totalSeconds = String.format("%.3f", stopWatch.getTotalTimeMillis() / 1000.0);
        tLog.info("总耗时: {} ms(约 {} 秒)", stopWatch.getTotalTimeMillis(), totalSeconds);
    }
}
