package com.vefuture.big_bottle.web.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vefuture.big_bottle.web.auth.entity.UserEntity;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangchao
 * @since 2025-04-17
 */
public interface UsersService extends IService<UserEntity> {

    // 假设这是创建用户的方法
    void createUser(String username, String rawPassword);

    // 验证密码的方法 (通常由 Spring Security 自动处理，但原理如下)
    boolean checkPassword(String rawPassword, String encodedPasswordFromDb);
}
