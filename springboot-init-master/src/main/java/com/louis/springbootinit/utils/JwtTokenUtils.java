package com.louis.springbootinit.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author NameGo
 */
public class JwtTokenUtils {

    // 密钥，用于签名JWT token
    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static final Map<String,String> LOGIN_TOKEN_MAP = new HashMap<>();

    public static String generateToken(String userAccount) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 设置token过期时间，例如1小时后过期
        long expMillis = nowMillis + 3600000;
        Date exp = new Date(expMillis);

        // 生成JWT token
        String token = Jwts.builder()
                .setSubject(userAccount) // 设置主题，即用户名
                .setIssuedAt(now) // 设置签发时间
                .setExpiration(exp) // 设置过期时间
                .signWith(KEY) // 使用密钥签名
                .compact();
        return token;
    }
}
