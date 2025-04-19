package com.vefuture.big_bottle.web.vefuture.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


import com.vefuture.big_bottle.web.vefuture.entity.qo.ReqBigBottleQo;
import com.vefuture.big_bottle.web.vefuture.entity.vo.ManageBigBottleVo;
import org.apache.ibatis.annotations.Mapper;

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

    List<ManageBigBottleVo> getManageBigBottleList(Page<ManageBigBottleVo> page, ReqBigBottleQo qo);

    Long getManageBigBottleCount(ReqBigBottleQo qo);
}
