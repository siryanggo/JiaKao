package com.mj.jk.common.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @auther yhjStart
 * @create 2022-03-29 23:34
 */
public class Md5 {
    public static String getEncode(String password) {
        byte[] bytes = password.getBytes(StandardCharsets.UTF_8);
        String str = DigestUtils.md5DigestAsHex(bytes);
        return str;
    }
}
