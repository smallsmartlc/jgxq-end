package com.jgxq.common.utils;

import com.jgxq.core.exception.SmartException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author LuCong
 * @since 2020-12-07
 **/
@Component
public class JwtUtil {
    @Value("${JWTParam.BASE64_ENCODE_SECRET_KEY}")
    public void setBase64EncodeSecretKey(String base64EncodeSecretKey) {
        JwtUtil.BASE64_ENCODE_SECRET_KEY = base64EncodeSecretKey;
    }


    @Value("${JWTParam.BEARER}")
    public void setBEARER(String bearer) {
        JwtUtil.BEARER = bearer;
    }

    @Value("${JWTParam.JG_COOKIE}")
    public void setJgCookie(String jgCookie) {
        JwtUtil.JG_COOKIE = jgCookie;
    }

    @Value("${JWTParam.KEY_LENGTH}")
    public void setKeyLength(int keyLength) {
        KEY_LENGTH = keyLength;
    }

    @Value("${JWTParam.cookieDomain}")
    public void setCookieDomain(String cookieDomain) {
        JwtUtil.cookieDomain = cookieDomain;
    }

    @Value("${JWTParam.EXP_DAYS}")
    public void setExpDays(Integer expDays){
        JwtUtil.expDays = expDays;
    }

    @Value("${JWTParam.EXP_TIME}")
    public void setExpTime(String expTime){
        JwtUtil.expTime = expTime;
    }

    /**
     * JWT过期时间：多少天后的凌晨4点
     */
    private static Integer expDays;

    /**
     * JWT过期时间：具体几点（24小时制）
     */
    private static String expTime;


    public static String cookieDomain;

    /**
     * 私钥
     */
    public static String BASE64_ENCODE_SECRET_KEY;


    public static String BEARER;

    /**
     * cookie名
     */
    public static String JG_COOKIE;

    /**
     * userKey长度
     */
    public static int KEY_LENGTH;


    /**
     * 生成TOKEN并返回
     *
     * @param email
     * @param userKey
     * @return
     */
    public static String generateToken(String email, String userKey) {
        //获取算法HS256
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(BASE64_ENCODE_SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("small", "smart")
//                .claim("role", role)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setId(userKey)
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expiration = getExpiration();
        if (expiration >= 0) {
            long expMillis = nowMillis + expiration;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        } else {
            throw new SmartException("500","expiration time is error");
        }

        //生成JWT
        return builder.compact();
    }


    /**
     * 校验token
     *
     * @param token
     * @throws ServletException
     */
    public static String validToken(String token) throws ServletException {
        try {
            final Claims claims = Jwts.parser().setSigningKey(BASE64_ENCODE_SECRET_KEY).parseClaimsJws(token).getBody();
            return claims.getId();
        } catch (ExpiredJwtException e1) {
            throw new ServletException("token expired");
        } catch (Exception e) {
            throw new ServletException("other token exception");
        }
    }

    /**
     * 获取请求头信息中的token，并校验其合法性
     *
     * @param authHeader
     * @return
     * @throws Exception
     */
    public static String dealRequests(String authHeader) throws Exception {
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            throw new ServletException("invalid authorization header");
        }
        String token = authHeader.substring(JwtUtil.BEARER.length());
        try {
            return JwtUtil.validToken(token);
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }


    /**
     *  mfc
     *  90天后的凌晨4点 - 当前时间 = 过期时间
     * @return 过期时间的毫秒值
     */
    public static long getExpiration() {
        LocalDate now = LocalDate.now();
        LocalDate expiration = now.plusDays(expDays);
        String dateString = expiration.getYear() + "-" + expiration.getMonth().getValue() + "-" + expiration.getDayOfMonth() + " " + expTime;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        ParsePosition position = new ParsePosition(0);
        Date future = dateFormat.parse(dateString,position);
        return future.getTime() - System.currentTimeMillis();
    }
}
