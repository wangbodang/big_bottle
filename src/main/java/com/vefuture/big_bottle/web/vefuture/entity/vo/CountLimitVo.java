package com.vefuture.big_bottle.web.vefuture.entity.vo;

import com.google.api.client.util.Value;
import lombok.Data;

/**
 * @author wangb
 * @date 2025/3/31
 * @description TODO: 类描述
 */
@Data
public class CountLimitVo {

    //配置最大的次数
    private Integer countMax;
    //该用户今天上传的次数
    private Integer countCurrent;
}
