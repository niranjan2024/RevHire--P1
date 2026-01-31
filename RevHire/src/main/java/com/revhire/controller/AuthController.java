package com.revhire.controller;

import com.revhire.exception.BusinessException;
import com.revhire.model.User;
import com.revhire.service.AuthService;

public class AuthController {

    private final AuthService authService = new AuthService();

    // ================= REGISTER =================
    public void register(String username, String email, String password, String question, String answer, String role) {

        try {
            authService.register(username, email, password, question, answer, role);
            System.out.println("✅ Registered Successfully");

        } catch (BusinessException e) {
            System.out.println("❌ Registration failed: " + e.getMessage());
        }
    }

    // ================= LOGIN =================
    public User login(String email, String password) {

        try {
            User user = authService.login(email, password);
            System.out.println("✅ Login successful");
            return user;

        } catch (BusinessException e) {
            System.out.println("❌ Login failed: " + e.getMessage());
            return null;
        }
    }

    // ================= FORGOT PASSWORD =================
    public void forgotPassword(String email, String answer, String newPassword) {

        try {
            authService.forgotPassword(email, answer, newPassword);
            System.out.println("✅ Password reset successful");

        } catch (BusinessException e) {
            System.out.println("❌ Password reset failed: " + e.getMessage());
        }
    }

    // ================= CHANGE PASSWORD =================
    public void changePassword(String email, String oldPassword, String newPassword) {

        try {
            authService.changePassword(email, oldPassword, newPassword);
            System.out.println("✅ Password changed successfully");

        } catch (BusinessException e) {
            System.out.println("❌ Change password failed: " + e.getMessage());
        }
    }
}
