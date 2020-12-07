package com.jgxq.common.utils;

/**
 * @author LuCong
 * @since 2020-12-07
 **/
public class UserUtils {
    public static String generateToken(String email, String userKey){
        return JwtUtil.BEARER + JwtUtil.generateToken(email, userKey);
    }
}
