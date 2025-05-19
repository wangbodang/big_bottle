package com.vefuture.big_bottle.web.vefuture.entity.qo;

import lombok.Data;

import java.time.OffsetDateTime;

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
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    //is_time_threshold retinfo_is_availd
    private Boolean isTimeThreshold;
    private Boolean retinfoIsAvaild;
    private String isDelete;
}
