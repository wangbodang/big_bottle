package com.vefuture.big_bottle.web.vefuture.service.task;

import cn.hutool.core.date.DateUtil;
import com.vefuture.big_bottle.web.websocket.WsSessionManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author wangb
 * @date 2025/4/29
 * @description TODO: 类描述
 */
@Slf4j
public class AsyncProcessTask extends Thread{
    private String  processId;
    private WsSessionManager ws;
    private String walletAddress;
    private String imgUrl;

    public AsyncProcessTask(String  processId, WsSessionManager manager, String walletAddress, String imgUrl){
        this.processId = processId;
        this.ws = manager;
        this.walletAddress = walletAddress;
        this.imgUrl = imgUrl;

    }

    @Override
    public void run() {
        log.info("-========++++++++++++++> 线程开始执行:{}", DateUtil.formatDateTime(new Date()));
        log.info("-========++++++++++++++> 休眠10秒钟!");
        try {
            Thread.sleep(10*1000);
            ws.sendToUser(walletAddress, "图片处理任务已完成 code:200; time:"+ DateUtil.formatDateTime(new Date()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("-========++++++++++++++> 线程结束执行:{}", DateUtil.formatDateTime(new Date()));
    }
}
