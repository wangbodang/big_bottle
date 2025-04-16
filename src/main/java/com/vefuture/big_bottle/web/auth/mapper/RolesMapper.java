package com.vefuture.big_bottle.web.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vefuture.big_bottle.web.auth.entity.RoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangchao
 * @since 2025-04-17
 */
@Mapper
public interface RolesMapper extends BaseMapper<RoleEntity> {
    List<RoleEntity> selectRolesByUserId(@Param("userId") Long userId);
}
