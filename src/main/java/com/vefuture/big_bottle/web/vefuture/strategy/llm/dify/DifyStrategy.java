package com.vefuture.big_bottle.web.vefuture.strategy.llm.dify;

import com.alibaba.fastjson.JSON;
import com.vefuture.big_bottle.common.config.prop.BigBottleProperties;
import com.vefuture.big_bottle.common.enums.ResultCode;
import com.vefuture.big_bottle.common.exception.BusinessException;
import com.vefuture.big_bottle.common.util.OkHttpUtil;
import com.vefuture.big_bottle.web.vefuture.service.IProcessLogService;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.LlmStrategy;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.coze.entity.BodyEntity;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.coze.entity.ParameterEntity;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.coze.entity.RetinfoCozeJson;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.dify.entity.RequestModelDTO;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.dify.entity.ResponseModelDTO;
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
 * @description Dify实现方法
 */
@Slf4j
@Component("dify")
public class DifyStrategy implements LlmStrategy {

    @Autowired
    private BigBottleProperties bigBottleProperties;
    @Autowired
    private IProcessLogService processLogService;
    //从单例class获取
    //private final OkHttpClient client = new OkHttpClient();
    private final OkHttpClient client = OkHttpUtil.getClient();

    @Override
    public RetinfoBigBottle call(RequestModel requestModel) {
        String processId = requestModel.getProcess_id();
        String walletAddress = requestModel.getWalletAddress();
        String imgUrl = requestModel.getImgUrl();
        String llm = requestModel.getLlm();
        try {
            //创建Log
            Long processLogId = processLogService.createLog(processId, walletAddress, imgUrl);
            //1 构造请求参数
            RequestModelDTO.ImgUrl imgUrlDTO = new RequestModelDTO.ImgUrl();
            imgUrlDTO.setUrl(imgUrl);
            RequestModelDTO.Inputs inputs = new RequestModelDTO.Inputs();
            inputs.setImg_url(imgUrlDTO);
            RequestModelDTO bodyEntity = new RequestModelDTO();
            bodyEntity.setInputs(inputs);

            //2 使用 Gson（或 Jackson 等）将实体转换为 JSON 字符串
            String jsonString = JSON.toJSONString(bodyEntity);

            //3 将JSON 字符串封装为 RequestBody
            RequestBody body = RequestBody.create(jsonString, MediaType.get("application/json; charset=utf-8"));

            //向AI模型发送请求并获取返回值
            Request request = new Request.Builder()
                    .header("Authorization", "Bearer "+bigBottleProperties.getDify().getDify_app_key())
                    .header("Content-Type", "application/json")
                    .url(bigBottleProperties.getDify().getDify_url())
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

            ResponseModelDTO retDifyDTO = JSON.parseObject(retJsonString, ResponseModelDTO.class);
            RetinfoBigBottle outputs = retDifyDTO.getData().getOutputs();

            return outputs;
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
