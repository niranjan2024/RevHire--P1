package com.revhire.util;

public class InputValidator {

    // Validates standard email format
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    // Strong password rules:
    // - Min 8 characters
    // - At least 1 uppercase
    // - At least 1 lowercase
    // - At least 1 digit
    // - At least 1 special character
    public static boolean isStrongPassword(String password) {
        if (password == null) return false;

        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[0-9].*") &&
                password.matches(".*[@#$%!].*");
    }
}
