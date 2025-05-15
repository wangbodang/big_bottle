package com.vefuture.big_bottle.web.vefuture.strategy.llm.coze;

import com.alibaba.fastjson.JSON;
import com.vefuture.big_bottle.common.config.prop.BigBottleProperties;
import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.common.enums.ResultCode;
import com.vefuture.big_bottle.common.exception.BusinessException;
import com.vefuture.big_bottle.common.util.BlockUtils;
import com.vefuture.big_bottle.common.util.OkHttpUtil;
import com.vefuture.big_bottle.web.vefuture.service.IProcessLogService;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.LlmStrategy;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.coze.entity.BodyEntity;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.coze.entity.ParameterEntity;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.coze.entity.RetinfoCozeJson;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.domain.RequestModel;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.domain.RetinfoBigBottle;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author wangb
 * @date 2025/4/26
 * @description Coze模型实现Strategy
 *
 */
@Slf4j
@Component("coze")
public class CozeStrategy implements LlmStrategy {
    @Autowired
    private BigBottleProperties bigBottleProperties;
    @Autowired
    private IProcessLogService processLogService;
    //从单例class获取
    //private final OkHttpClient client = new OkHttpClient();
    private final OkHttpClient client = OkHttpUtil.getClient();

    //用Coze实现策略
    @Override
    public RetinfoBigBottle call(RequestModel requestModel) {
        String processId = requestModel.getProcess_id();
        String walletAddress = requestModel.getWalletAddress();
        String imgUrl = requestModel.getImgUrl();
        String llm = requestModel.getLlm();
        String ipAddress = requestModel.getIpAddress();
        try {
            //创建Log
            Long processLogId = processLogService.createLog(ipAddress,processId, walletAddress, imgUrl);
            //1 构造请求参数
            ParameterEntity parameterEntity = new ParameterEntity();
            parameterEntity.setImg_url(imgUrl);
            BodyEntity bodyEntity = new BodyEntity();
            bodyEntity.setWorkflow_id(bigBottleProperties.getCoze().getCoze_workflow_id());
            bodyEntity.setParameters(parameterEntity);

            //2 使用 Gson（或 Jackson 等）将实体转换为 JSON 字符串
            String jsonString = JSON.toJSONString(bodyEntity);

            //3 将JSON 字符串封装为 RequestBody
            RequestBody body = RequestBody.create(jsonString, MediaType.get("application/json; charset=utf-8"));

            //向AI模型发送请求并获取返回值
            Request request = new Request.Builder()
                    .header("Authorization", bigBottleProperties.getCoze().getCoze_token())
                    .header("Content-Type", "application/json")
                    .url(bigBottleProperties.getCoze().getCoze_url())
                    .post(body)
                    .build();

            //更新AI开始时间
            processLogService.updateAiStartTime(processLogId, LocalDateTime.now());
            Response response = client.newCall(request).execute();
            //更新AI结束时间
            processLogService.updateAiEndTime(processLogId, LocalDateTime.now());

            String retJsonString = response.body().string();
            log.info("---> 返回值为:{}", retJsonString);
            //更新AI返回日志
            processLogService.updateAiReturnMsg(processLogId, LocalDateTime.now(), retJsonString);

            RetinfoCozeJson retinfoCozeJson = JSON.parseObject(retJsonString, RetinfoCozeJson.class);
            String contentStr = retinfoCozeJson.getData();
            log.info("---> 返回的票据信息为:[{}]", contentStr);

            RetinfoBigBottle bigBottle = JSON.parseObject(contentStr, RetinfoBigBottle.class);

            return bigBottle;
        } catch (BusinessException businessException){
            log.error("===> 业务异常:{}", businessException.getMessage());
            throw new BusinessException(ResultCode.INTERNAL_ERROR.getCode(), ResultCode.INTERNAL_ERROR.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("===> 业务异常:{}", e.getMessage());
            throw new RuntimeException("业务异常", e);
        }


    }
}
