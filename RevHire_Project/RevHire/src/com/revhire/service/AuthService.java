package com.revhire.service;

import java.util.Scanner;

import com.revhire.dao.UserDAO;
import com.revhire.model.User;
import com.revhire.util.InputValidator;
import com.revhire.util.PasswordUtil;

public class AuthService {

    private UserDAO userDAO = new UserDAO();

    // ================= REGISTER =================
    public void register(Scanner sc) {

        System.out.print("Username: ");
        String username = sc.next();

        System.out.print("Email: ");
        String email = sc.next();

        System.out.print("Password: ");
        String password = sc.next();

        sc.nextLine();
        System.out.print("Security Question: ");
        String question = sc.nextLine();

        System.out.print("Security Answer: ");
        String answer = sc.nextLine();

        System.out.print("Role (JOB_SEEKER / EMPLOYER): ");
        String role = sc.next();

        //  Validation FIRST
        if (!InputValidator.isValidEmail(email)) {
            System.out.println("Invalid email format");
            return;
        }

        if (!InputValidator.isStrongPassword(password)) {
            System.out.println(
                    "Password must be at least 8 chars with uppercase, digit & special character"
            );
            return;
        }

        //  Hash password
        String hashedPassword = PasswordUtil.hashPassword(password);

        int id = userDAO.register(
                username, email, hashedPassword, role, question, answer
        );

        System.out.println(id > 0 ? "Registered Successfully" : "Registration Failed");
    }


    // ================= LOGIN =================
    public void login(Scanner sc) {

        System.out.print("Email: ");
        String email = sc.next();

        System.out.print("Password: ");
        String password = sc.next();

        String hashedPassword = PasswordUtil.hashPassword(password);

        User user = userDAO.login(email, hashedPassword);

        if (user == null) {
            System.out.println("Invalid Login");
            return;
        }

        if (user.role.equalsIgnoreCase("JOB_SEEKER")) {
            new JobSeekerService().start(user.userId, user.username);
        } else {
            new EmployerService().start(user.userId);
        }
    }

    // ================= FORGOT PASSWORD =================
    public void forgotPassword(Scanner sc) {

        System.out.print("Registered Email: ");
        String email = sc.next();

        sc.nextLine();
        System.out.print("Security Answer: ");
        String answer = sc.nextLine();

        System.out.print("New Password: ");
        String newPassword = sc.next();

        if (!InputValidator.isStrongPassword(newPassword)) {
            System.out.println("New password is not strong enough");
            return;
        }

        String hashedPassword = PasswordUtil.hashPassword(newPassword);

        boolean success =
                userDAO.resetPassword(email, answer, hashedPassword);

        System.out.println(
                success ? "Password reset successful"
                        : "Invalid email or security answer"
        );
    }

    // ================= CHANGE PASSWORD =================
    public void changePassword(Scanner sc) {

        System.out.print("Email: ");
        String email = sc.next();

        System.out.print("Current Password: ");
        String oldPassword = sc.next();

        System.out.print("New Password: ");
        String newPassword = sc.next();

        System.out.print("Confirm New Password: ");
        String confirmPassword = sc.next();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match!");
            return;
        }

        if (!InputValidator.isStrongPassword(newPassword)) {
            System.out.println("New password is not strong enough");
            return;
        }

        String oldHashed = PasswordUtil.hashPassword(oldPassword);
        String newHashed = PasswordUtil.hashPassword(newPassword);

        boolean success =
                userDAO.changePassword(email, oldHashed, newHashed);

        System.out.println(
                success ? "Password changed successfully!"
                        : "Invalid email or current password"
        );
    }
}
