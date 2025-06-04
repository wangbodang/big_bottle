package com.vefuture.big_bottle.web.vefuture.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vefuture.big_bottle.common.exception.BusinessException;
import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import com.vefuture.big_bottle.web.vefuture.entity.BlackList;
import com.vefuture.big_bottle.web.vefuture.entity.ProcessLog;
import com.vefuture.big_bottle.web.vefuture.entity.qo.BigBottleQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.qo.BlackListQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.vo.ManageBigBottleVo;
import com.vefuture.big_bottle.web.vefuture.mapper.BVefutureBigBottleMapper;
import com.vefuture.big_bottle.web.vefuture.mapper.BlackListMapper;
import com.vefuture.big_bottle.web.vefuture.mapper.ProcessLogMapper;
import com.vefuture.big_bottle.web.vefuture.service.IBlackListService;
import com.vefuture.big_bottle.web.vefuture.service.IManageBiBottleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangb
 * @date 2025/4/19
 * @description 管理饮料列表Service
 */
@Slf4j
@Service
public class ManageBiBottleServiceImpl implements IManageBiBottleService {

    @Autowired
    private BVefutureBigBottleMapper bigBottleMapper;
    @Autowired
    private IBlackListService blackListService;
    @Autowired
    private BlackListMapper blackListMapper;
    @Autowired
    private ProcessLogMapper processLogMapper;

    @Override
    public Page<ManageBigBottleVo> getBigBottleList(HttpServletRequest request, Page<ManageBigBottleVo> page, BigBottleQueryDTO qo) {
        //处理时间
        //处理endDate:
        if (qo.getEndDate() != null) {
            qo.setEndDate(qo.getEndDate().plusSeconds(86399)); // 或 plusDays(1).minusSeconds(1)
        }

        Page<ManageBigBottleVo> manageBigBottleList = bigBottleMapper.getManageBigBottleList(page, qo);
        //设置图像名字
        List<ManageBigBottleVo> records = manageBigBottleList.getRecords();
        records.forEach(manageBigBottleVo -> {
            String imgUrl = manageBigBottleVo.getImgUrl();
            String ipAddress = manageBigBottleVo.getIpAddress();
            manageBigBottleVo.setImgName(imgUrl.substring(imgUrl.lastIndexOf("/")+1));
            //设置数量和钱包地址
            //setAssociated(manageBigBottleVo, ipAddress);
        });

        // 批量处理 setAssociated，替代原 forEach 中的方法
        // 注意：setAssociatedBatch(records) 必须在 forEach 外部执行！
        // 否则会形成 O(N^2) 查询，严重拖慢大数据统计场景。
        setAssociatedBatch(records);
        return page;
    }

    // 批量处理 setAssociated，替代原 forEach 中的方法
    public void setAssociatedBatch(List<ManageBigBottleVo> records) {
        if (records == null || records.isEmpty()) return;

        // 1. 提取 IP（去重、非空）
        Set<String> ipSet = records.stream()
                .map(ManageBigBottleVo::getIpAddress)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (ipSet.isEmpty()) return;

        // 2. 分批查询 ProcessLog
        List<ProcessLog> allLogs = new ArrayList<>();
        List<String> ipList = new ArrayList<>(ipSet);
        int totalSize = ipList.size();
        int batchSize = Math.max(200, Math.min(1000, totalSize / 10)); // 动态设置批大小

        for (int i = 0; i < totalSize; i += batchSize) {
            List<String> batch = ipList.subList(i, Math.min(i + batchSize, totalSize));
            List<ProcessLog> part = processLogMapper.selectList(
                    new LambdaQueryWrapper<ProcessLog>().in(ProcessLog::getIpAddress, batch)
            );
            allLogs.addAll(part);
        }

        if (allLogs.isEmpty()) return;

        // 3. 构建 Map<ip, List<wallet_address>>
        Map<String, List<String>> ipWalletListMap = new HashMap<>(1024);
        for (ProcessLog log : allLogs) {
            String ip = log.getIpAddress();
            String wallet = log.getWalletAddress();
            if (ip == null || wallet == null) continue;
            ipWalletListMap
                    .computeIfAbsent(ip, k -> new ArrayList<>())
                    .add(wallet);
        }

        // 4. 写回每条记录（去重 + 拼接）
        for (ManageBigBottleVo vo : records) {
            String ip = vo.getIpAddress();
            List<String> wallets = ipWalletListMap.get(ip);

            if (wallets == null || wallets.isEmpty()) {
                vo.setAssociatedCount(0);
                vo.setAssociatedAddresses("");
            } else {
                Set<String> walletSet = new HashSet<>(wallets);
                vo.setAssociatedCount(walletSet.size());
                vo.setAssociatedAddresses(String.join("$", walletSet));
            }
        }
    }


    //这个查询太慢 forEach + setAssociated	可能 N 次数据库/API 请求，极有可能是瓶颈 ❗
    //这就是典型的 N+1 查询问题，即：
    //主查询一次性查出了 N 条数据
    //然后每条都要再查一次数据库
    //获取IP关联的地址数和地址
    private void setAssociated(ManageBigBottleVo vo, String ipAddress) {
        LambdaQueryWrapper<ProcessLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProcessLog::getIpAddress, ipAddress);
        List<ProcessLog> processLogs = processLogMapper.selectList(queryWrapper);
        if(CollectionUtil.isEmpty(processLogs)){
            vo.setAssociatedAddresses("");
            vo.setAssociatedCount(0);
            return;
        }
        Set<String> addressSet = new HashSet<>();
        for (int i = 0; i < processLogs.size(); i++) {
            addressSet.add(processLogs.get(i).getWalletAddress());
        }
        vo.setAssociatedCount(addressSet.size());
        String addressJoin = String.join("$", addressSet);
        vo.setAssociatedAddresses(addressJoin);
    }

    @Override
    public Page<BlackList> getBlackList(HttpServletRequest request, Page<BlackList> page, BlackListQueryDTO dto) {
        String walletAddress = StrUtil.isBlank(dto.getWalletAddress()) ? "" : dto.getWalletAddress().toLowerCase().trim();
        List<Integer> blackTypes = dto.getBlackTypes();
        log.info("===> 查询条件为:{}-{}", walletAddress, blackTypes);
        LambdaQueryWrapper<BlackList> queryWrapper = Wrappers.<BlackList>lambdaQuery()
                // 当 walletAddress 非空时才加 like
                .like(StrUtil.isNotBlank(walletAddress), BlackList::getWalletAddress, walletAddress)
                // 当 blackTypes 列表不为 null 且不空时，才加 in 条件
                .in(blackTypes != null && !blackTypes.isEmpty(),
                        BlackList::getBlackType,
                        blackTypes)
                // 固定按 createTime 降序
                .orderByDesc(BlackList::getCreateTime);
        Page<BlackList> blackLists = blackListService.page(page, queryWrapper);
        return blackLists;
    }

    //根据ids查询出小票信息
    @Override
    public List<BVefutureBigBottle> getDetailsByIds(List<String> ids) {
        // 1) 把 String 转成 Long
        List<Long> longIds = ids.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());

        // 2) 一次性批量查询
        List<BVefutureBigBottle> resultList = bigBottleMapper.selectBatchIds(longIds);

        log.info("===>>> ids:{} 小票列表为: {}", ids, resultList);
        return resultList;
    }

    /***
     * 根据IDs 作废相关的小票.
     * @param rawIds
     */
    @Override
    public void invalidateReceiptsByIds(List<Object> rawIds) {
        List<Long> ids = rawIds.stream()
                .map(Object::toString)
                .map(Long::valueOf)
                .collect(Collectors.toList());
        int updated = bigBottleMapper.update(
                null,
                new LambdaUpdateWrapper<BVefutureBigBottle>()
                        .in(BVefutureBigBottle::getId, ids)
                        .set(BVefutureBigBottle::getRetinfoIsAvaild, false)
        );
        log.info("作废成功，共更新 " + updated + " 条记录");
    }

    @Override
    public void addWalletAddressToBlacklist(String walletAddress, Integer type) {
        LambdaQueryWrapper<BlackList> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlackList::getWalletAddress, walletAddress);
        List<BlackList> blackLists = blackListMapper.selectList(queryWrapper);
        if(CollectionUtil.isEmpty(blackLists)){
            String walletAddressLower = walletAddress.trim().toLowerCase();
            BlackList blackList = new BlackList();
            blackList.setWalletAddress(walletAddressLower);
            blackList.setBlackType(type);
            blackListMapper.insert(blackList);
        }
        BlackList blackList = blackLists.get(0);
        if(blackList.getBlackType() == 5){
            blackList.setBlackType(type);
            blackListMapper.updateById(blackList);
        }
    }

    //导出CSV
    @Override
    public void exportCsv(HttpServletRequest request, HttpServletResponse response, BigBottleQueryDTO dto) {
        String nowStr = DateUtil.formatDateTime(new Date()).replaceAll(" ", "_").replaceAll(":", "_");

        String fileName = "B3ty_Token_" + nowStr + ".csv";
        File csvFile = new File(fileName);
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
            writer.write("ID,钱包地址,图片链接,饮料种类,饮料数量,黑名单类型");
            writer.newLine();

            // 查询数据（你应该根据 dto 查询出数据）
            Page<ManageBigBottleVo> page = new Page<>(dto.getCurrent(), dto.getSize());
            Page<ManageBigBottleVo> bigBottleList = getBigBottleList(request, page, dto);// 替换为你实际的方法

            // 写入数据行
            for (ManageBigBottleVo item : bigBottleList.getRecords()) {
                writer.write(String.format("%s,%s,%s,%d,%d,%d",
                        item.getIds(),
                        item.getWalletAddress(),
                        item.getImgUrl(),
                        item.getDrinkKind(),
                        item.getDrinkAmount(),
                        item.getBlackType()
                ));
                writer.newLine();
            }

            writer.flush();
            writer.close();

        } catch (IOException e) {
            throw new BusinessException("导出失败!");
        }
    }

    @Override
    public void blackIp(BlackListQueryDTO dto) {
        String  ipAddress = dto.getIpAddress();
        if(StrUtil.isBlank(ipAddress)){
            throw new BusinessException("参数IP地址为空");
        }
        //查出IP关联的所有地址
        LambdaQueryWrapper<ProcessLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProcessLog::getIpAddress, ipAddress);
        List<ProcessLog> processLogs = processLogMapper.selectList(queryWrapper);
        if(CollectionUtil.isEmpty(processLogs)){
            return;
        }
        Set<String> addressSet = new HashSet<>();
        for (int i = 0; i < processLogs.size(); i++) {
            addressSet.add(processLogs.get(i).getWalletAddress());
        }
        addressSet.forEach(address -> {
            LambdaQueryWrapper<BlackList> blackListQueryWrapper = new LambdaQueryWrapper<>();
            blackListQueryWrapper.eq(BlackList::getWalletAddress, address);
            List<BlackList> blackLists = blackListMapper.selectList(blackListQueryWrapper);
            if(CollectionUtil.isEmpty(blackLists)){
                BlackList temp = new BlackList();
                temp.setWalletAddress(address);
                temp.setBlackType(1);
                blackListMapper.insert(temp);
                return;
            }
            if(CollectionUtil.isNotEmpty(blackLists)){
                BlackList blackList = blackLists.get(0);
                if(blackList.getBlackType() != 1){
                    blackList.setBlackType(1);
                    blackListMapper.updateById(blackList);
                }
            }

        });
    }
}
