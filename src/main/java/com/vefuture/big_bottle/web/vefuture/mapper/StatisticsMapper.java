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

    //统计指定日期使用的钱包 不在黑名单内的
    Integer queryWalletAddressCount(@Param("qo") StatisticsQueryDTO dto);
    //统计指定日期处理的小票数 不在黑名单内的
    Integer getTotalImageCount(@Param("qo") StatisticsQueryDTO dto);

    Integer getPassedAddressCount(@Param("qo") StatisticsQueryDTO dto);

    Integer getPassedReceiptCount(@Param("qo") StatisticsQueryDTO dto);

    Integer getUnpassedAddressCount(@Param("qo") StatisticsQueryDTO dto);

    Integer getunpassedReceiptCount(@Param("qo") StatisticsQueryDTO dto);
}
