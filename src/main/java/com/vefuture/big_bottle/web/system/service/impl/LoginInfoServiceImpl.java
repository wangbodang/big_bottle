package com.vefuture.big_bottle.web.system.service.impl;

import com.vefuture.big_bottle.web.system.entity.LoginInfo;
import com.vefuture.big_bottle.web.system.mapper.LoginInfoMapper;
import com.vefuture.big_bottle.web.system.service.LoginInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统访问记录 服务实现类
 * </p>
 *
 * @author wangchao
 * @since 2025-04-23
 */
@Service
public class LoginInfoServiceImpl extends ServiceImpl<LoginInfoMapper, LoginInfo> implements LoginInfoService {

    @Override
    public void insertLogininfor(LoginInfo logininfor) {

    }
}
