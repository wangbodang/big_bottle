package com.vefuture.big_bottle.web.vefuture.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author wangb
 * @date 2025/3/15
 * @description 饮料名称 数量 容量
 */
@Data
@AllArgsConstructor
public class RetinfoDrink {
    //饮料名称
    private String retinfoDrinkName;
    //饮料容量
    private Integer retinfoDrinkCapacity;
    //饮料数量
    private Integer retinfoDrinkAmout;
}
