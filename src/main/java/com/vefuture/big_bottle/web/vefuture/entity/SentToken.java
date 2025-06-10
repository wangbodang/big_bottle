package com.vefuture.big_bottle.web.vefuture.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangchao
 * @since 2025-06-11
 */
@Getter
@Setter
@ToString
@TableName("b_vef_sent_token")
public class SentToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 钱包地址
     */
    @TableField("wallet_address")
    private String walletAddress;

    /**
     * 事务值
     */
    @TableField("transaction_value")
    private String transactionValue;

    /**
     * 发送的token数量
     */
    @TableField("sent_token")
    private BigDecimal sentToken;

    /**
     * 图片地址
     */
    @TableField("img_url")
    private String imgUrl;

    /**
     * 事务的全地址
     */
    @TableField("transaction_url")
    private String transactionUrl;

    @TableField("remark")
    private String remark;

    @TableField("is_delete")
    private Boolean isDelete;

    @TableField("create_id")
    private Long createId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private OffsetDateTime createTime;

    @TableField("update_id")
    private Long updateId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private OffsetDateTime updateTime;
}
