package com.vefuture.big_bottle.web.vefuture.entity.qo;

import lombok.Data;

/**
 * @author wangb
 * @date 2025/3/15
 * @description 前端请求接收VO
 */
@Data
public class BigBottleQueryDTO {

    private int current;
    private int size;


    //ve链钱包地址
    private String walletAddress;
    //图片地址
    private String imgUrl;
    //
    private String startDate;
    private String endDate;
}
