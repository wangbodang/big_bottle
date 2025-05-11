package com.vefuture.big_bottle.web.vefuture.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


import com.vefuture.big_bottle.web.vefuture.entity.qo.BigBottleQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.vo.ManageBigBottleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * vefuture bigbottle 基本信息表 Mapper 接口
 * </p>
 *
 * @author wangchao
 * @since 2025-03-15
 */
@Mapper
public interface BVefutureBigBottleMapper extends BaseMapper<BVefutureBigBottle> {

    Page<ManageBigBottleVo> getManageBigBottleList(Page<ManageBigBottleVo> page, BigBottleQueryDTO qo);

    /*
        用另外一种写法
        LambdaQueryWrapper<VeFutureBigBottle> wrapper = Wrappers.<VeFutureBigBottle>lambdaQuery()
        .select(
           VeFutureBigBottle::getId,
           VeFutureBigBottle::getWalletAddress,
           // 聚合函数要写成字符串
           SQLMethods.min("id") + " AS id",
                   SQLMethods.boolAnd("retinfo_is_availd") + " AS retinfo_is_availd",
                   //其他的聚合字段
                   )
                   .groupBy(VeFutureBigBottle::getWalletAddress, VeFutureBigBottle::getImgUrl)
        .orderByDesc(SQLMethods.min("id"));

        IPage<ManageBigBottleVo> page = new Page<>(current, size);
        return bigBottleMapper.selectVoPage(page, wrapper);
    * */
    IPage<ManageBigBottleVo> selectVoPage(Page<?> page,
                                          @Param(Constants.WRAPPER) Wrapper<BVefutureBigBottle> ew);

    Long getManageBigBottleCount(BigBottleQueryDTO qo);
}
