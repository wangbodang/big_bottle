package com.vefuture.big_bottle.web.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vefuture.big_bottle.web.system.entity.OperLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 操作日志记录 Mapper 接口
 * </p>
 *
 * @author wangchao
 * @since 2025-04-23
 */
@Mapper
public interface OperLogMapper extends BaseMapper<OperLog> {

    /* 获取日志ID */
    Long getNextSequnceId();
}
