package com.vefuture.big_bottle.web.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vefuture.big_bottle.web.system.entity.SysConfig;
import com.vefuture.big_bottle.web.system.entity.query.SysConfigQueryDTO;
import com.vefuture.big_bottle.web.system.mapper.SysConfigMapper;
import com.vefuture.big_bottle.web.system.service.SysConfigService;
import com.vefuture.big_bottle.web.vefuture.entity.BlackList;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 参数配置表 服务实现类
 * </p>
 *
 * @author wangchao
 * @since 2025-05-19
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

    @Override
    public Page<SysConfig> getSysConfigList(HttpServletRequest request, Page<SysConfig> page, SysConfigQueryDTO dto) {

        LambdaQueryWrapper<SysConfig> queryWrapper = Wrappers.<SysConfig>lambdaQuery()
                    // 当 walletAddress 非空时才加 like
                    //.like(StrUtil.isNotBlank(walletAddress), BlackList::getWalletAddress, walletAddress)
                    // 当 blackTypes 列表不为 null 且不空时，才加 in 条件
                    //.in(blackTypes != null && !blackTypes.isEmpty(), BlackList::getBlackType, blackTypes)
                    // 固定按 createTime 降序
                .orderByDesc(SysConfig::getId);
        return this.page(page, queryWrapper);
    }
}
