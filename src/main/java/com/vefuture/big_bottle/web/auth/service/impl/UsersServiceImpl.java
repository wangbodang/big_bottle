package com.vefuture.big_bottle.web.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vefuture.big_bottle.web.auth.entity.UserEntity;
import com.vefuture.big_bottle.web.auth.mapper.UsersMapper;
import com.vefuture.big_bottle.web.auth.service.UsersService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangchao
 * @since 2025-04-17
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, UserEntity> implements UsersService {

}
