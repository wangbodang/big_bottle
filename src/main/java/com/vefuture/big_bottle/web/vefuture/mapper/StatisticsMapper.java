package com.vefuture.big_bottle.web.vefuture.mapper;

import com.vefuture.big_bottle.web.vefuture.entity.qo.StatisticsQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wangb
 * @date 2025/5/17
 * @description TODO: 类描述
 */
@Mapper
public interface StatisticsMapper {
    Integer queryWalletAddressCount(@Param("qo") StatisticsQueryDTO dto);
}
