package com.vefuture.big_bottle.web.vefuture.service;

import com.vefuture.big_bottle.web.vefuture.entity.ProcessLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangchao
 * @since 2025-04-18
 */
public interface IProcessLogService extends IService<ProcessLog> {

    Long createLog(String ipAddress, String processId, String walletAddress, String imgUrl);

    void updateAiStartTime(Long processLogId, LocalDateTime now);

    void updateAiEndTime(Long processLogId, LocalDateTime now);

    void updateAiReturnMsg(Long processLogId, LocalDateTime now, String retJsonString);
}
