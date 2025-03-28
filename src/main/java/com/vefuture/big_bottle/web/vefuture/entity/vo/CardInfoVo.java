package com.vefuture.big_bottle.web.vefuture.entity.vo;

import lombok.Data;

/**
 * @author wangb
 * @date 2025/3/28
 * @description 首页卡返回饮料名称和数量
 */
@Data
public class CardInfoVo {
    String drinkName;
    Integer drinkCapacity;
    Integer drinkAmout;

    Integer points;
}
