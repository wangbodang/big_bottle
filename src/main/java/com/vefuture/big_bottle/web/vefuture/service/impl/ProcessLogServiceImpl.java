package com.vefuture.big_bottle.web.vefuture.service.impl;

import com.vefuture.big_bottle.web.vefuture.entity.ProcessLog;
import com.vefuture.big_bottle.web.vefuture.mapper.ProcessLogMapper;
import com.vefuture.big_bottle.web.vefuture.service.IProcessLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangchao
 * @since 2025-04-18
 */
@Slf4j
@Service
public class ProcessLogServiceImpl extends ServiceImpl<ProcessLogMapper, ProcessLog> implements IProcessLogService {
    @Override
    public Long createLog(String processId, String walletAddress, String imgUrl) {
        ProcessLog processLog = new ProcessLog();
        processLog.setProcessId(processId);
        processLog.setWalletAddress(walletAddress);
        processLog.setImgUrl(imgUrl);
        processLog.setIsDelete(false);
        LocalDateTime now = LocalDateTime.now();
        processLog.setCreateTime(now);
        processLog.setUpdateTime(now);
        this.save(processLog);
        Long id = processLog.getId();
        log.info("---> 生成的日志ID:[{}]", id);
        return id;
    }

    @Override
    public void updateAiStartTime(Long processLogId, LocalDateTime now) {
        ProcessLog processLog = new ProcessLog();
        processLog.setId(processLogId);
        processLog.setAiProcessStartTime(now);
        processLog.setUpdateTime(now);
        this.updateById(processLog);
    }

    @Override
    public void updateAiEndTime(Long processLogId, LocalDateTime now) {
        ProcessLog processLog = new ProcessLog();
        processLog.setId(processLogId);
        processLog.setAiProcessEndTime(now);
        processLog.setUpdateTime(now);
        this.updateById(processLog);
    }

    @Override
    public void updateAiReturnMsg(Long processLogId, LocalDateTime now, String retJsonString) {
        ProcessLog processLog = new ProcessLog();
        processLog.setId(processLogId);
        processLog.setAiProcessEndTime(now);
        processLog.setAiReturnMsg(retJsonString);
        processLog.setUpdateTime(now);
        this.updateById(processLog);
    }
}
