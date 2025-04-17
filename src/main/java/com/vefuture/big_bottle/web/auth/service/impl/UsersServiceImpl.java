package com.vefuture.big_bottle.web.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vefuture.big_bottle.web.auth.entity.UserEntity;
import com.vefuture.big_bottle.web.auth.mapper.UsersMapper;
import com.vefuture.big_bottle.web.auth.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired // 注入在 SecurityConfig 中定义的 Bean
    private PasswordEncoder passwordEncoder;

    // 假设这是创建用户的方法
    @Override
    public void createUser(String username, String rawPassword) {
        // ... 创建用户对象 ...
        UserEntity user = new UserEntity();
        user.setUsername(username);

        // 使用注入的 passwordEncoder 来加密密码
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword); // 设置加密后的密码

        // ... 设置其他用户属性 ...

        // ... 保存用户到数据库 (userRepository.save(user)) ...
    }

    // 验证密码的方法 (通常由 Spring Security 自动处理，但原理如下)
    @Override
    public boolean checkPassword(String rawPassword, String encodedPasswordFromDb) {
        // 使用同一个 encoder 的 matches 方法来比较
        return passwordEncoder.matches(rawPassword, encodedPasswordFromDb);
    }
}
