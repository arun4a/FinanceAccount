package com.laxmifinance.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * Legacy-compatible password encryption. Existing databases keep working
 * because the key, salt and iteration count are unchanged.
 */
public final class CryptoUtil {

    private static final String KEY = "1vENKychANDUarUN2Arun";
    private static final byte[] SALT = {
            (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
            (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
    };
    private static final int ITERATION_COUNT = 19;
    private static final String ALGORITHM = "PBEWithMD5AndDES";

    private CryptoUtil() {
    }

    public static String encrypt(String plainText) {
        try {
            Cipher cipher = cipher(Cipher.ENCRYPT_MODE);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to encrypt value", e);
        }
    }

    public static String decrypt(String encryptedText) {
        try {
            Cipher cipher = cipher(Cipher.DECRYPT_MODE);
            byte[] decrypted = cipher.doFinal(Base64.getMimeDecoder().decode(encryptedText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to decrypt value", e);
        }
    }

    /**
     * Accepts legacy encrypted values and, for rows created by the old
     * registration screen, plain-text values.
     */
    public static boolean matches(String rawPassword, String storedPassword) {
        if (rawPassword == null || storedPassword == null) {
            return false;
        }
        try {
            return rawPassword.equals(decrypt(storedPassword));
        } catch (RuntimeException e) {
            return rawPassword.equals(storedPassword);
        }
    }

    private static Cipher cipher(int mode) throws Exception {
        KeySpec keySpec = new PBEKeySpec(KEY.toCharArray(), SALT, ITERATION_COUNT);
        SecretKey key = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(keySpec);
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);
        Cipher cipher = Cipher.getInstance(key.getAlgorithm());
        cipher.init(mode, key, paramSpec);
        return cipher;
    }
}
