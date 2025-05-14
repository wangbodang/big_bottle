package com.vefuture.big_bottle.web.vefuture.entity.qo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wangb
 * @date 2025/5/7
 * @description TODO: 类描述
 */
@Data
public class BlackListQueryDTO {
    private int current;
    private int size;

    private Long blackId;
    // BigBottleQueryDTO 中的字段，比如：
    //@JsonProperty("walletAddress") // ✅ 强制匹配 JSON 字段
    private String walletAddress;
    private Integer blackType;
    private List<Integer> blackTypes;
}
