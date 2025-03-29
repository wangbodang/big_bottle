package com.vefuture.big_bottle.web.vefuture.entity.qo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @author wangb
 * @date 2025/3/15
 * @description 前端请求接收VO
 */
@Data
public class ReqBigBottleQo {

    //ve链钱包地址
    private String walletAddress;
    //图片地址
    private String imgUrl;
}
