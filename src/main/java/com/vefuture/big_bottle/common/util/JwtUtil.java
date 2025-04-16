package com.vefuture.big_bottle.common.util;

import com.vefuture.big_bottle.common.config.prop.JwtProperties;

import com.vefuture.big_bottle.web.auth.entity.LoginUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtUtil {

    @Autowired
    private JwtProperties jwtProperties;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        log.info("===> JWT配置的选项[{}]-[{}]", jwtProperties.getSecret(), jwtProperties.getExpiration());
        byte[] keyBytes = Base64.getDecoder().decode(jwtProperties.getSecret());
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // 生成 Token（传对象）
    public String generateToken(LoginUser user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("uid", user.getUid())
                .claim("role", user.getRole())
                .claim("permissions", user.getPermissions())
                .claim("email", user.getEmail())
                .claim("isAdmin", user.getIsAdmin())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            log.warn("Token 校验失败", e);
            return false;
        }
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsername(String token) {
        return parseToken(token).getSubject();
    }

    public LoginUser getLoginUser(String token) {
        Claims claims = parseToken(token);
        return new LoginUser(
                claims.get("uid", Long.class),
                claims.getSubject(),
                claims.get("role", String.class),
                (List<String>) claims.get("permissions"),
                claims.get("email", String.class),
                claims.get("isAdmin", Boolean.class)
        );
    }
}
