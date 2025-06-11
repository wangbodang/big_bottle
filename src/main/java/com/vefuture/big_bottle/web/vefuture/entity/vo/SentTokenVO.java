package com.vefuture.big_bottle.web.vefuture.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * @author wangb
 * @date 2025/6/11
 * @description TODO: 类描述
 */
@Data
public class SentTokenVO {
    private Long id;

    /**
     * 钱包地址
     */
    private String walletAddress;

    /**
     * 事务值
     */
    private String transactionValue;

    /**
     * 发送的token数量
     */
    private BigDecimal sentToken;

    /**
     * 图片地址
     */
    private String imgUrl;

    private String  imgName;

    /**
     * 事务的全地址
     */
    private String transactionUrl;

    private String remark;

    private Boolean isDelete;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    private OffsetDateTime createTime;
}
