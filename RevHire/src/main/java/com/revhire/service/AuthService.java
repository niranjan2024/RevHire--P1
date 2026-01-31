package com.revhire.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revhire.dao.UserDAO;
import com.revhire.exception.BusinessException;
import com.revhire.exception.DatabaseException;
import com.revhire.model.User;
import com.revhire.util.InputValidator;
import com.revhire.util.PasswordUtil;

public class AuthService {

    private static final Logger logger =
            LogManager.getLogger(AuthService.class);

    private final UserDAO userDAO = new UserDAO();

    // ================= REGISTER =================
    public void register(String username,
                         String email,
                         String password,
                         String question,
                         String answer,
                         String role) {

        logger.info("Register request received | email={}, role={}", email, role);

        // Validation
        if (!InputValidator.isValidEmail(email)) {
            logger.warn("Invalid email format | email={}", email);
            throw new BusinessException("Invalid email format");
        }

        if (!InputValidator.isStrongPassword(password)) {
            logger.warn("Weak password attempt | email={}", email);
            throw new BusinessException(
                    "Password must be at least 8 chars with uppercase, digit & special character"
            );
        }

        try {
            String hashedPassword = PasswordUtil.hashPassword(password);

            int userId = userDAO.register(
                    username, email, hashedPassword, role, question, answer
            );

            if (userId <= 0) {
                logger.error("User registration failed | email={}", email);
                throw new BusinessException("Registration failed");
            }

            logger.info("User registered successfully | userId={}, email={}", userId, email);

        } catch (DatabaseException e) {
            logger.error("Database error during registration | email={}", email, e);
            throw new BusinessException("Unable to register user. Please try again.");
        }
    }

    // ================= LOGIN =================
    public User login(String email, String password) {

        logger.info("Login attempt | email={}", email);

        try {
            String hashedPassword = PasswordUtil.hashPassword(password);

            User user = userDAO.login(email, hashedPassword);

            if (user == null) {
                logger.warn("Invalid login credentials | email={}", email);
                throw new BusinessException("Invalid email or password");
            }

            logger.info(
                    "Login successful | userId={}, role={}",
                    user.userId, user.role
            );

            return user;

        } catch (DatabaseException e) {
            logger.error("Database error during login | email={}", email, e);
            throw new BusinessException("Unable to login. Please try again.");
        }
    }

    // ================= FORGOT PASSWORD =================
    public void forgotPassword(String email,
                               String answer,
                               String newPassword) {

        logger.info("Forgot password request | email={}", email);

        if (!InputValidator.isStrongPassword(newPassword)) {
            logger.warn("Weak new password | email={}", email);
            throw new BusinessException("New password is not strong enough");
        }

        try {
            String hashedPassword = PasswordUtil.hashPassword(newPassword);

            boolean success =
                    userDAO.resetPassword(email, answer, hashedPassword);

            if (!success) {
                logger.warn("Password reset failed | email={}", email);
                throw new BusinessException("Invalid email or security answer");
            }

            logger.info("Password reset successful | email={}", email);

        } catch (DatabaseException e) {
            logger.error("Database error during password reset | email={}", email, e);
            throw new BusinessException("Unable to reset password. Please try again.");
        }
    }

    // ================= CHANGE PASSWORD =================
    public void changePassword(String email,
                               String oldPassword,
                               String newPassword) {

        logger.info("Change password request | email={}", email);

        if (!InputValidator.isStrongPassword(newPassword)) {
            logger.warn("Weak new password | email={}", email);
            throw new BusinessException("New password is not strong enough");
        }

        try {
            boolean success =
                    userDAO.changePassword(
                            email,
                            PasswordUtil.hashPassword(oldPassword),
                            PasswordUtil.hashPassword(newPassword)
                    );

            if (!success) {
                logger.warn("Change password failed | email={}", email);
                throw new BusinessException("Invalid email or current password");
            }

            logger.info("Password changed successfully | email={}", email);

        } catch (DatabaseException e) {
            logger.error("Database error during password change | email={}", email, e);
            throw new BusinessException("Unable to change password. Please try again.");
        }
    }
}
