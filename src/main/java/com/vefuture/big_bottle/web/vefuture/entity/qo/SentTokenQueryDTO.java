package com.vefuture.big_bottle.web.vefuture.entity.qo;

import com.vefuture.big_bottle.common.domain.model.BaseQuery;
import lombok.Data;
import lombok.ToString;

/**
 * @author wangb
 * @date 2025/6/11
 * @description TODO: 类描述
 */
@Data
@ToString(callSuper = true)
public class SentTokenQueryDTO extends BaseQuery {
    private String walletAddress;
}
