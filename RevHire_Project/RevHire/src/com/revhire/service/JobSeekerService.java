package com.revhire.service;

import java.util.Scanner;
import com.revhire.dao.*;

public class JobSeekerService {

    private JobSeekerDAO seekerDAO = new JobSeekerDAO();
    private JobDAO jobDAO = new JobDAO();
    private ApplicationDAO applicationDAO = new ApplicationDAO();
    private ResumeDAO resumeDAO = new ResumeDAO();

    public void start(int userId, String username) {

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== JOB SEEKER DASHBOARD ===");
            System.out.println("1. Profile");
            System.out.println("2. Resume");
            System.out.println("3. Search Jobs");
            System.out.println("4. My Applications");
            System.out.println("5. Withdraw Application");
            System.out.println("6. Notifications");
            System.out.println("7. Logout");

            int choice = sc.nextInt();

            switch (choice) {

                case 1 -> profileMenu(userId, sc);

                case 2 -> resumeMenu(userId, sc);

                case 3 -> jobDAO.searchJobs(sc, userId);

                case 4 -> applicationDAO.viewMyApplications(userId);

                case 5 -> {
                    System.out.print("Application ID: ");
                    int appId = sc.nextInt();
                    applicationDAO.withdrawApplication(appId, sc);
                }

                case 6 -> {
                    NotificationDAO notificationDAO = new NotificationDAO();
                    notificationDAO.viewNotifications(userId);

                    System.out.print("Mark any notification as read? (yes/no): ");
                    String ans = sc.next();

                    if (ans.equalsIgnoreCase("yes")) {
                        System.out.print("Enter Notification ID: ");
                        int nid = sc.nextInt();
                        notificationDAO.markAsRead(nid);
                        System.out.println("Notification marked as read.");
                    }
                }

                case 7 -> {
                    System.out.println("Logged out successfully");
                    return;
                }

                default -> System.out.println("Invalid choice");
            }
        }
    }

    // ================= PROFILE SUB MENU =================

    private void profileMenu(int userId, Scanner sc) {

        while (true) {
            System.out.println("\n--- PROFILE MENU ---");
            System.out.println("1. Complete Profile");
            System.out.println("2. Update Profile");
            System.out.println("3. Back");

            int ch = sc.nextInt();

            switch (ch) {
                case 1 -> seekerDAO.completeProfile(userId, sc);

                case 2 -> seekerDAO.updateProfile(userId, sc);

                case 3 -> {
                    return;
                }

                default -> System.out.println("Invalid choice");
            }
        }
    }

    // ================= RESUME SUB MENU =================

    private void resumeMenu(int userId, Scanner sc) {

        while (true) {
            System.out.println("\n--- RESUME MENU ---");
            System.out.println("1. Upload Resume");
            System.out.println("2. Update Resume");
            System.out.println("3. Back");

            int ch = sc.nextInt();

            switch (ch) {
                case 1 -> resumeDAO.uploadResume(userId, sc);

                case 2 -> resumeDAO.updateResume(userId, sc);

                case 3 -> {
                    return;
                }

                default -> System.out.println("Invalid choice");
            }
        }
    }

}
