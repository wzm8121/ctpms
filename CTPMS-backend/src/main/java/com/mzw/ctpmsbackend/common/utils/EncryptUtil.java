/**
 * @Author: David Ma
 * @Description: AES加密工具类
 * @Date: 2025-03-19
 */
package com.mzw.ctpmsbackend.common.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptUtil {

    // 修正 key 长度为 16 字节（AES-128）
    private static String fixKeyLength(String key) {
        int desiredLength = 16;
        if (key.length() < desiredLength) {
            StringBuilder sb = new StringBuilder(key);
            while (sb.length() < desiredLength) {
                sb.append("0");
            }
            return sb.toString();
        } else if (key.length() > desiredLength) {
            return key.substring(0, desiredLength);
        }
        return key;
    }

    // 加密方法
    public static String encrypt(String key, String value) {
        try {
            String fixedKey = fixKeyLength(key);
            SecretKeySpec secretKey = new SecretKeySpec(fixedKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("加密失败", e);
        }
    }

    // 解密方法
    public static String decrypt(String key, String encryptedValue) {
        try {
            String fixedKey = fixKeyLength(key);
            SecretKeySpec secretKey = new SecretKeySpec(fixedKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedValue);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("解密失败", e);
        }
    }
}

