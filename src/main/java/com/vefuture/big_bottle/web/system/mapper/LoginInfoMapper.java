package com.vefuture.big_bottle.web.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vefuture.big_bottle.web.system.entity.LoginInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统访问记录 Mapper 接口
 * </p>
 *
 * @author wangchao
 * @since 2025-04-23
 */
@Mapper
public interface LoginInfoMapper extends BaseMapper<LoginInfo> {

}
