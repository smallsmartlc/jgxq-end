package com.jgxq.common.utils;

import com.jgxq.front.define.VerificationCodeTypeEnum;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author LuCong
 * @since 2020-12-07
 **/
public class LoginUtils {

    /**
     * 判断密码规则是否合法，字母、数字、特殊字符最少2种组合（不能有中文和空格）
     *
     * @param str
     * @return
     */
    private static Pattern passwordRegex = Pattern.compile("(?!.*[\\u4E00-\\u9FA5\\s])(?!^[a-zA-Z]+$)(?!^[\\d]+$)(?!^[^a-zA-Z\\d]+$)^.{6,20}$", Pattern.CASE_INSENSITIVE);
    public static boolean checkPassword(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }

        Matcher matcher = passwordRegex.matcher(str);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static String generateToken(String email, String userKey){
        return JwtUtil.BEARER + JwtUtil.generateToken(email, userKey);
    }

    public static String getRandomUserKey(int length) {
        if (length <= 0) {
            return null;
        } else {
            StringBuilder builder = new StringBuilder(length + 1);
            int rand = ThreadLocalRandom.current().nextInt("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".length());
            builder.append("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(rand));

            for(int i = 1; i < length; ++i) {
                rand = ThreadLocalRandom.current().nextInt("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".length());
                builder.append("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(rand));
            }

            return builder.toString();
        }
    }

    private static final String CODE_STR = "0123456789";
    public static String createValidateCode(int num) {
        StringBuilder code = new StringBuilder(num);
        for (int i = 0; i < num; i++) {
            char ch = CODE_STR.charAt(new Random().nextInt(CODE_STR.length()));
            code.append(ch);
        }
        return code.toString();
    }

    public static String emailToRedisKey(String email, VerificationCodeTypeEnum type){
        String typeStr = "_"+type+"_";
        return "JG"+typeStr+email;
    }
}
