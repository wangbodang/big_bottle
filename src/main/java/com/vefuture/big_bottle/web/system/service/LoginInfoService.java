package com.vefuture.big_bottle.web.system.service;

import com.vefuture.big_bottle.web.system.entity.LoginInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统访问记录 服务类
 * </p>
 *
 * @author wangchao
 * @since 2025-04-23
 */
public interface LoginInfoService extends IService<LoginInfo> {

    void insertLogininfor(LoginInfo logininfor);
}
