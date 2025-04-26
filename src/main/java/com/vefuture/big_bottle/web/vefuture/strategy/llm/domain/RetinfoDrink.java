package com.vefuture.big_bottle.web.vefuture.strategy.llm.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangb
 * @date 2025/3/15
 * @description 饮料名称 数量 容量
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetinfoDrink {
    /*
    //饮料名称
    private String retinfoDrinkName;
    //饮料容量
    private Integer retinfoDrinkCapacity;
    //饮料数量
    private Integer retinfoDrinkAmout;
    */

    /**
     * 饮料名称
     */
    @JsonProperty("retinfoDrinkName")  // Jackson支持
    @JSONField(name = "retinfoDrinkName") // Fastjson支持
    private String retinfoDrinkName;

    /**
     * 饮料容量（单位：毫升）
     */
    @JsonProperty("retinfoDrinkCapacity")
    @JSONField(name = "retinfoDrinkCapacity")
    private Integer retinfoDrinkCapacity;

    /**
     * 饮料数量
     */
    @JsonProperty("retinfoDrinkAmout")
    @JSONField(name = "retinfoDrinkAmout")
    private Integer retinfoDrinkAmout;

    // ✅ 兼容旧的字段：retinfoDrinkAmout（错字）
    @JsonProperty("retinfoDrinkAmount")
    @JSONField(name = "retinfoDrinkAmount")
    public void setRetinfoDrinkAmount(Integer wrongAmount) {
        this.retinfoDrinkAmout = wrongAmount;
    }
}
