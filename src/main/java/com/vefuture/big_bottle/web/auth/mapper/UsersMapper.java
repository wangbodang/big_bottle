package com.vefuture.big_bottle.web.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vefuture.big_bottle.web.auth.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangchao
 * @since 2025-04-17
 */
@Mapper
public interface UsersMapper extends BaseMapper<UserEntity> {

}
