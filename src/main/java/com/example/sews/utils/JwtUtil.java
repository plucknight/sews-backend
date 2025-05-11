package com.example.sews.utils;

import com.example.sews.dto.Admin;
import com.example.sews.dto.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.util.*;

/**
 * JWT 工具类
 */
public class JwtUtil {

    // JWT 有效期，默认 60 分钟
    public static final Long JWT_TTL = 60 * 60 * 1000L;

    // JWT 密钥明文，需确保密钥长度足够（至少 256 位）

    /**
     * 生成 UUID，作为 JWT 的唯一标识
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成 JWT
     *
     * @param subject JWT 中存放的数据（通常是 JSON 格式）
     * @return 生成的 JWT 字符串
     */
    public static String createJWT(String subject) {
        return createJWT(subject, null);
    }

    /**
     * 生成 JWT
     *
     * @param subject   JWT 中存放的数据（通常是 JSON 格式）
     * @param ttlMillis JWT 的有效期（毫秒）
     * @return 生成的 JWT 字符串
     */
    public static String createJWT(String subject, Long ttlMillis) {
        return getJwtBuilder(subject, ttlMillis, getUUID()).compact();
    }

    /**
     * 生成 JWT
     *
     * @param id        JWT 的唯一标识
     * @param subject   JWT 中存放的数据（通常是 JSON 格式）
     * @param ttlMillis JWT 的有效期（毫秒）
     * @return 生成的 JWT 字符串
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        return getJwtBuilder(subject, ttlMillis, id).compact();
    }

    /**
     * 构建 JwtBuilder
     *
     * @param subject   JWT 中存放的数据
     * @param ttlMillis JWT 的有效期
     * @param uuid      JWT 的唯一标识
     * @return JwtBuilder 实例
     */
    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 如果未指定有效期，则使用默认值
        if (ttlMillis == null) {
            ttlMillis = JWT_TTL;
        }

        // 计算过期时间
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);

        // 使用 HS256 算法和固定密钥生成 JWT
        return Jwts.builder()
                .setId(uuid)                      // 设置唯一标识
                .setSubject(subject)              // 设置主题（存放的数据）
                .setIssuer("myh")                 // 设置签发者
                .setIssuedAt(now)                 // 设置签发时间
                .signWith(generalKey(), SignatureAlgorithm.HS256) // 使用 HS256 算法签名
                .setExpiration(expDate);          // 设置过期时间
    }

    /**
     * 生成固定密钥
     *
     * @return SecretKey 实例
     */
    public static SecretKey generalKey() {
        // 将密钥字符串解码为字节数组
        byte[] decodedKey = Base64.getDecoder().decode("SEWS");
        // 如果密钥长度不足，填充到 32 字节
        if (decodedKey.length < 32) {
            decodedKey = Arrays.copyOf(decodedKey, 32);
        }
        // 使用 HmacSHA256 算法生成 SecretKey
        return Keys.hmacShaKeyFor(decodedKey);
    }

    /**
     * 解析 JWT
     *
     * @param jwt JWT 字符串
     * @return Claims 对象，包含 JWT 中的有效数据
     * @throws Exception 如果 JWT 无效或签名不匹配
     */
    public static Claims parseJWT(String jwt) throws Exception {
        return Jwts.parserBuilder()
                .setSigningKey(generalKey()) // 使用相同的密钥验证签名
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    /**
     * 获取当前登录的管理员信息
     *
     * @param authentication Spring Security 的 Authentication 对象
     * @return Admin 对象
     */
    public static Admin getCurrentLoginAdmin(Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return loginUser.getAdmin();
    }

    /**
     * 生成随机字符串（用于测试或其他用途）
     *
     * @return 随机字符串
     */
    public static String generateRandomString() {
        int length = 8;
        StringBuilder stringBuilder = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomInt = random.nextInt(26);
            char randomChar = (char) ('a' + randomInt);
            if (random.nextBoolean()) {
                randomChar = Character.toUpperCase(randomChar);
            }
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }
}