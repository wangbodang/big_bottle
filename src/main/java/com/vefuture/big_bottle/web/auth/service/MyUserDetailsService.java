package com.vefuture.big_bottle.web.auth.service;

import com.vefuture.big_bottle.web.auth.entity.RoleEntity;
import com.vefuture.big_bottle.web.auth.entity.UserEntity;
import com.vefuture.big_bottle.web.auth.mapper.ResourcesMapper;
import com.vefuture.big_bottle.web.auth.mapper.RolesMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangb
 * @date 2025/4/16
 * @description =================================================...............................
 */
@Slf4j
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersService usersService;
    @Autowired
    private RolesMapper rolesMapper;
    @Autowired
    private ResourcesMapper resourcesMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("===> 用户名为:[{}]", username);
        // 查询用户
        UserEntity user = usersService.lambdaQuery()
                .eq(UserEntity::getUsername, username)
                .eq(UserEntity::getIsDelete, false)
                .one();

        if (user == null) {
            log.info("===> 用户为null");
            throw new UsernameNotFoundException("用户不存在");
        }
        log.info("===>数据库密码:[{}]", user.getPassword());
        // 查询用户所有角色
        List<RoleEntity> roles = rolesMapper.selectRolesByUserId(user.getId());
        List<String> roleCodes = roles.stream()
                .map(RoleEntity::getRoleCode)
                .collect(Collectors.toList());

        // 查询用户权限码（可选）
        List<String> authorities = resourcesMapper.selectResourceCodesByUserId(user.getId());

        // 构建权限列表（角色 + 权限）
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        roleCodes.forEach(code -> grantedAuthorities.add(new SimpleGrantedAuthority(code)));
        authorities.forEach(code -> grantedAuthorities.add(new SimpleGrantedAuthority(code)));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getEnabled(), true, true, true,
                grantedAuthorities
        );
    }
}
