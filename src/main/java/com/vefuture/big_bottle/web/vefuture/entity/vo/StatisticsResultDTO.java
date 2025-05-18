package com.vefuture.big_bottle.web.vefuture.entity.vo;

import lombok.Data;

/**
 * @author wangb
 * @date 2025/5/17
 * @description TODO: 类描述
 */
@Data
public class StatisticsResultDTO {

    private Integer walletAddressCount = 0;
    private Integer availdReceiptCount = 0;
    private Integer invalidReceiptCount = 0;
}
