package com.tool.auth;

import com.drew.metadata.mp4.media.Mp4UuidBoxDescriptor;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;

/**
 * @author wangb
 * @date 2025/3/30
 * @description TODO: 类描述
 */
@Slf4j
public class AuthTool {

    //DBggVjX8FdEO8ckmVLpVjATP6t6OJFgeVIzfpDKSWLI=
    @Test
    public void test01(){
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 生成安全密钥
        System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));
    }
    //判断密码是否合规
    @Test
    public void test02(){
        String raw = "123456";
        String encoded = "$2a$10$/6xpo132t7IXh9.EbiX7a.JvYKSnbeoStrq8BPC.WVgOlgnsRUwW6";
        boolean match = new BCryptPasswordEncoder().matches(raw, encoded);
        System.out.println("匹配结果：" + match); // 应该打印 true
    }

    //生成密码方法
    @Test
    public void test03(){
        String rawPassword = "BottleBig";
        String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);
        System.out.println("加密后的密码: " + encodedPassword);
    }

    @Test void test04(){
        //private final Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        //System.out.println("生成的 JWT Key（Base64 编码）:");
        log.info("---> 生成的 JWT Key（Base64 编码）:\n{}", base64Key);
    }
}
