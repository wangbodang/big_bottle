package com.vefuture.big_bottle.web.vefuture.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author wangb
 * @date 2025/5/19
 * @description 发币时用的对象
 */
@Data
public class B3tyTokenTransDto {
    private String  walletAddress;
    private BigDecimal b3tyToken;
    private String imgUrl;
}
