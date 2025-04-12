package com.tool.auth;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
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
}
