package com.vefuture.big_bottle.web.vefuture.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vefuture.big_bottle.web.vefuture.entity.SentToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangchao
 * @since 2025-06-11
 */
@Mapper
public interface SentTokenMapper extends BaseMapper<SentToken> {

    //批量插入
    void batchInsert(@Param("list") List<SentToken> tokenList);
}
