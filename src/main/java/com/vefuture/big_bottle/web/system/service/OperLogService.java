package com.vefuture.big_bottle.web.system.service;

import com.vefuture.big_bottle.web.system.entity.OperLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 操作日志记录 服务类
 * </p>
 *
 * @author wangchao
 * @since 2025-04-23
 */
public interface OperLogService extends IService<OperLog> {

    void insertOperlog(OperLog operLog);

    void updateOperlogById(OperLog operLog);

    Long nextSequenceId();
}
