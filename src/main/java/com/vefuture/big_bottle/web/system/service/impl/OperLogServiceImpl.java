package com.vefuture.big_bottle.web.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.vefuture.big_bottle.common.interceptor.AccessLogInterceptor;
import com.vefuture.big_bottle.common.util.BlockUtils;
import com.vefuture.big_bottle.web.system.entity.OperLog;
import com.vefuture.big_bottle.web.system.mapper.OperLogMapper;
import com.vefuture.big_bottle.web.system.service.OperLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志记录 服务实现类
 * </p>
 *
 * @author wangchao
 * @since 2025-04-23
 */
@Slf4j
@Service
public class OperLogServiceImpl extends ServiceImpl<OperLogMapper, OperLog> implements OperLogService {
    @Override
    public void insertOperlog(OperLog operLog) {
        //记录操作日志
        this.save(operLog);
    }

    @Override
    public void updateOperlogById(OperLog operLog) {
        this.updateById(operLog);
    }

    @Override
    public Long nextSequenceId() {
        return this.baseMapper.getNextSequnceId();
    }
}
