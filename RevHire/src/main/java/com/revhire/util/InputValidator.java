package com.revhire.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InputValidator {

    private static final Logger logger =
            LogManager.getLogger(InputValidator.class);

    // ================= EMAIL VALIDATION =================
    public static boolean isValidEmail(String email) {

        if (email == null) {
            logger.warn("Email validation failed: email is null");
            return false;
        }

        boolean valid =
                email.matches("^[A-Za-z0-9+_.-]+@(.+)$");

        if (!valid) {
            logger.warn("Email validation failed: invalid format | email={}", email);
        } else {
            logger.debug("Email validation passed | email={}", email);
        }

        return valid;
    }

    // Strong password rules:
    // - Min 8 characters
    // - At least 1 uppercase
    // - At least 1 lowercase
    // - At least 1 digit
    // - At least 1 special character

    // ================= PASSWORD VALIDATION =================
    public static boolean isStrongPassword(String password) {

        if (password == null) {
            logger.warn("Password validation failed: password is null");
            return false;
        }

        boolean valid =
                password.length() >= 8 &&
                        password.matches(".*[A-Z].*") &&
                        password.matches(".*[a-z].*") &&
                        password.matches(".*[0-9].*") &&
                        password.matches(".*[@#$%!].*");

        if (!valid) {
            logger.warn("Password validation failed: weak password");
        } else {
            logger.debug("Password validation passed");
        }

        return valid;
    }
}
