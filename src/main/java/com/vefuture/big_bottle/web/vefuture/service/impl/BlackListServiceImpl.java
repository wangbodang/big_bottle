package com.vefuture.big_bottle.web.vefuture.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vefuture.big_bottle.common.util.BlockUtils;
import com.vefuture.big_bottle.web.vefuture.entity.BlackList;
import com.vefuture.big_bottle.web.vefuture.entity.qo.BlackListQueryDTO;
import com.vefuture.big_bottle.web.vefuture.mapper.BlackListMapper;
import com.vefuture.big_bottle.web.vefuture.service.IBlackListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * vefuture 黑名单列表 服务实现类
 * </p>
 *
 * @author wangchao
 * @since 2025-05-05
 */
@Slf4j
@Service
public class BlackListServiceImpl extends ServiceImpl<BlackListMapper, BlackList> implements IBlackListService {

    //验证是否在黑名单里有该钱包地址 若有则返回真 否则返回假
    @Override
    public boolean isBlacklisted(String walletAddress) {
        log.info("--->>>校验黑名单里的的wallet_address为:{}", BlockUtils.getBlock(walletAddress));
        LambdaQueryWrapper<BlackList> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlackList::getWalletAddress, walletAddress.trim().toLowerCase());
        //传入一个 List
        List<Integer> types = Arrays.asList(1, 3);
        queryWrapper.in(BlackList::getBlackType, types);
        List<BlackList> list = this.list(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)){
            return true;
        }
        return false;
    }

    //根据ID恢复拉黑的钱包地址
    @Override
    public void recoverBlack(BlackListQueryDTO dto) {
        BlackList blackList = new BlackList();
        blackList.setId(dto.getBlackId());
        blackList.setWalletAddress(dto.getWalletAddress());
        blackList.setBlackType(0);
        blackList.setUpdateTime(new Date());
        this.updateById(blackList);
    }
}
