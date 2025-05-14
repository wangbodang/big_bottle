package com.vefuture.big_bottle.web.vefuture.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import com.vefuture.big_bottle.web.vefuture.entity.BlackList;
import com.vefuture.big_bottle.web.vefuture.entity.qo.BigBottleQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.qo.BlackListQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.vo.ManageBigBottleVo;
import com.vefuture.big_bottle.web.vefuture.mapper.BVefutureBigBottleMapper;
import com.vefuture.big_bottle.web.vefuture.mapper.BlackListMapper;
import com.vefuture.big_bottle.web.vefuture.service.IBlackListService;
import com.vefuture.big_bottle.web.vefuture.service.IManageBiBottleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    public Page<ManageBigBottleVo> getBigBottleList(HttpServletRequest request, Page<ManageBigBottleVo> page, BigBottleQueryDTO qo) {
        Page<ManageBigBottleVo> manageBigBottleList = bigBottleMapper.getManageBigBottleList(page, qo);
        //设置图像名字
        List<ManageBigBottleVo> records = manageBigBottleList.getRecords();
        records.forEach(manageBigBottleVo -> {
            String imgUrl = manageBigBottleVo.getImgUrl();
            manageBigBottleVo.setImgName(imgUrl.substring(imgUrl.lastIndexOf("/")+1));
        });
        return page;
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
}
