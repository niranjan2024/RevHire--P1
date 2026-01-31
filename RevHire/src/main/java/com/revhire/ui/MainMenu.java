package com.revhire.ui;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revhire.exception.BusinessException;
import com.revhire.model.User;
import com.revhire.service.AuthService;
import com.revhire.service.EmployerService;
import com.revhire.service.JobSeekerService;

public class MainMenu {

    private static final Logger logger =
            LogManager.getLogger(MainMenu.class);

    private final AuthService auth = new AuthService();

    public void start() {

        Scanner sc = new Scanner(System.in);

        logger.info("RevHire application started");

        while (true) {
            try {
                System.out.println("\n=== REVHIRE ===");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Forgot Password");
                System.out.println("4. Change Password");
                System.out.println("5. Exit");

                int ch = sc.nextInt();
                logger.info("Main menu option selected | choice={}", ch);

                switch (ch) {

                    // ================= REGISTER =================
                    case 1 -> {
                        try {
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

                            logger.info("Register request from UI | email={}, role={}", email, role);

                            auth.register(
                                    username, email, password,
                                    question, answer, role
                            );

                            System.out.println("Registered Successfully");
                            logger.info("User registered successfully | email={}", email);

                        } catch (BusinessException e) {
                            logger.warn("Registration failed | message={}", e.getMessage());
                            System.out.println(e.getMessage());
                        }
                    }

                    // ================= LOGIN =================
                    case 2 -> {
                        try {
                            System.out.print("Email: ");
                            String email = sc.next();

                            System.out.print("Password: ");
                            String password = sc.next();

                            logger.info("Login request from UI | email={}", email);

                            User user = auth.login(email, password);

                            logger.info(
                                    "Login successful | userId={}, role={}",
                                    user.userId, user.role
                            );

                            if (user.role.equalsIgnoreCase("JOB_SEEKER")) {
                                new JobSeekerService()
                                        .start(user.userId, user.username);
                            } else {
                                new EmployerService()
                                        .start(user.userId);
                            }

                        } catch (BusinessException e) {
                            logger.warn("Login failed | message={}", e.getMessage());
                            System.out.println(e.getMessage());
                        }
                    }

                    // ================= FORGOT PASSWORD =================
                    case 3 -> {
                        try {
                            System.out.print("Registered Email: ");
                            String email = sc.next();

                            sc.nextLine();
                            System.out.print("Security Answer: ");
                            String answer = sc.nextLine();

                            System.out.print("New Password: ");
                            String newPassword = sc.next();

                            logger.info("Forgot password request from UI | email={}", email);

                            auth.forgotPassword(email, answer, newPassword);

                            System.out.println("Password reset successful");
                            logger.info("Password reset successful | email={}", email);

                        } catch (BusinessException e) {
                            logger.warn("Password reset failed | message={}", e.getMessage());
                            System.out.println(e.getMessage());
                        }
                    }

                    // ================= CHANGE PASSWORD =================
                    case 4 -> {
                        try {
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
                                logger.warn("Password mismatch during change | email={}", email);
                                break;
                            }

                            logger.info("Change password request from UI | email={}", email);

                            auth.changePassword(email, oldPassword, newPassword);

                            System.out.println("Password changed successfully!");
                            logger.info("Password changed successfully | email={}", email);

                        } catch (BusinessException e) {
                            logger.warn("Change password failed | message={}", e.getMessage());
                            System.out.println(e.getMessage());
                        }
                    }

                    // ================= EXIT =================
                    case 5 -> {
                        logger.info("Application exited by user");
                        System.out.println("Thank you for using RevHire!");
                        System.exit(0);
                    }

                    default -> {
                        logger.warn("Invalid main menu choice | choice={}", ch);
                        System.out.println("Invalid choice");
                    }
                }

            } catch (Exception e) {
                logger.error("Invalid input in MainMenu", e);
                System.out.println("Invalid input. Please try again.");
                sc.nextLine(); // clear buffer
            }
        }
    }
}
