package com.vefuture.big_bottle.web.vefuture.service;

import com.vefuture.big_bottle.web.vefuture.entity.BlackList;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * vefuture 黑名单列表 服务类
 * </p>
 *
 * @author wangchao
 * @since 2025-05-05
 */
public interface IBlackListService extends IService<BlackList> {
    boolean isBlacklisted(String walletAddress);
}
