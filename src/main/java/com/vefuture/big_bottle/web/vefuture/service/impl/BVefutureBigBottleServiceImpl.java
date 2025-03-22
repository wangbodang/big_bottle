package com.vefuture.big_bottle.web.vefuture.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.common.enums.ResultCode;
import com.vefuture.big_bottle.common.exception.BusinessException;
import com.vefuture.big_bottle.common.util.OkHttpUtil;
import com.vefuture.big_bottle.common.vechain.BodyEntity;
import com.vefuture.big_bottle.common.vechain.ParameterEntity;
import com.vefuture.big_bottle.web.vefuture.entity.*;
import com.vefuture.big_bottle.web.vefuture.mapper.BVefutureBigBottleMapper;
import com.vefuture.big_bottle.web.vefuture.service.BVefutureBigBottleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

/**
 * <p>
 * vefuture bigbottle 基本信息表 服务实现类
 * </p>
 *
 * @author wangchao
 * @since 2025-03-15
 */
@Slf4j
@Service
public class BVefutureBigBottleServiceImpl extends ServiceImpl<BVefutureBigBottleMapper, BVefutureBigBottle> implements BVefutureBigBottleService {
    //todo 改为配置
    String cozeUrl = "https://api.coze.com/v1/workflow/run";
    //todo 改为配置
    //String token = "Bearer pat_2cG2zJt8n9T1uqNDyVJ1lUS0h3R09NetFeWHDsPoLHDPgZW94u0DKN0kgIdc48Lx"; //我的
    String token = "Bearer pat_lUnXXnZwd75NmjB5GIXtZAo6uR5rEhDziooiD2AxF9d12HFqTIFSV1uWKSRK8Bdd";  //八个大大的
    //todo 以后改为从单例class获取
    //private final OkHttpClient client = new OkHttpClient();
    private final OkHttpClient client = OkHttpUtil.getClient();

    /**
     *
     * @param  bigBottleVo
     * @return  返回值说明
     */
    @Override
    public ApiResponse processReceipt(ReqBigBottleVo bigBottleVo) {

        //钱包地址和图片地址
        String walletAddress = bigBottleVo.getWalletAddress();
        String imgUrl = bigBottleVo.getImgUrl();

        //构造请求参数
        ParameterEntity parameterEntity = new ParameterEntity();
        parameterEntity.setImg_url(imgUrl);
        BodyEntity bodyEntity = new BodyEntity();
        bodyEntity.setParameters(parameterEntity);

        // 3. 使用 Gson（或 Jackson 等）将实体转换为 JSON 字符串
        String jsonString = JSON.toJSONString(bodyEntity);
        log.info("---> 请求参数为:{}", jsonString);
        // 4. 将 JSON 字符串封装为 RequestBody
        RequestBody body = RequestBody.create(jsonString, MediaType.get("application/json; charset=utf-8"));

        try {
            Request request = new Request.Builder()
                    .header("Authorization", token)
                    .header("Content-Type", "application/json")
                    .url(cozeUrl)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();

            String retJsonString = response.body().string();
            log.info("---> 返回值为:{}", retJsonString);

            RetinfoLLMJson retinfoLLMJson = JSON.parseObject(retJsonString, RetinfoLLMJson.class);
            String contentStr = retinfoLLMJson.getData();
            log.info("---> 返回的票据信息为:[{}]", contentStr);

            RetinfoBigBottle bigBottle = JSON.parseObject(contentStr, RetinfoBigBottle.class);

            if(!bigBottle.getRetinfoIsAvaild()){
                log.info("---> 该票据信息不完整");
                return ApiResponse.error(ResultCode.RECEIPT_ERR_UNAVAILABLE.getCode(), ResultCode.RECEIPT_ERR_UNAVAILABLE.getMessage());
            }

            //todo 存到数据库
            saveToDb(walletAddress, imgUrl, bigBottle);
        } catch (IOException e) {
            //throw new BusinessException(430, "业务异常:"+e.getMessage(), e);
            e.printStackTrace();
            log.error("===> 业务异常:{}", e.getMessage());
            return ApiResponse.error(ResultCode.INTERNAL_ERROR.getCode(), ResultCode.INTERNAL_ERROR.getMessage());
        }
        return ApiResponse.success();
    }

    //存储到
    private void saveToDb(String walletAddress, String imgUrl, RetinfoBigBottle retinfoBigBottle) {

        ArrayList<RetinfoDrink> drinkList = retinfoBigBottle.getDrinkList();
        drinkList.forEach(drink -> {
            BVefutureBigBottle bigBottle = new BVefutureBigBottle();
            //公共信息
            bigBottle.setWalletAddress(walletAddress);
            bigBottle.setImgUrl(imgUrl);
            bigBottle.setRetinfoIsAvaild(retinfoBigBottle.getRetinfoIsAvaild());
            bigBottle.setRetinfoReceiptTime(retinfoBigBottle.getRetinfoReceiptTime());
            //饮料信息
            bigBottle.setRetinfoDrinkName(drink.getRetinfoDrinkName());
            bigBottle.setRetinfoDrinkCapacity(drink.getRetinfoDrinkCapacity());
            bigBottle.setRetinfoDrinkAmout(drink.getRetinfoDrinkAmout());
            this.save(bigBottle);
        });
    }
}
