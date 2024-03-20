package com.backend.springstore.security;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class MD5Utils {
    public static String encodePassword(String password, String salt, int times) {
        password = password + salt;
        for (int i = 0; i < times; i++) {
            password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        }
        return password;
    }
}
