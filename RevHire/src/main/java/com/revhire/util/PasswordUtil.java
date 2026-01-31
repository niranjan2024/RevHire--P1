package com.revhire.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PasswordUtil {

    private static final Logger logger =
            LogManager.getLogger(PasswordUtil.class);

    // ================= HASH PASSWORD =================
    public static String hashPassword(String password) {

        if (password == null) {
            logger.error("Password hashing failed: password is null");
            throw new IllegalArgumentException("Password cannot be null");
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            logger.error("Password hashing failed: SHA-256 algorithm not found", e);
            throw new RuntimeException("Password hashing failed", e);
        }
    }
}
