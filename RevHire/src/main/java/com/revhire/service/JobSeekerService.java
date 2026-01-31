package com.revhire.service;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revhire.dao.ApplicationDAO;
import com.revhire.dao.JobDAO;
import com.revhire.dao.JobSeekerDAO;
import com.revhire.dao.NotificationDAO;
import com.revhire.dao.ResumeDAO;
import com.revhire.exception.DatabaseException;

public class JobSeekerService {

    private static final Logger logger =
            LogManager.getLogger(JobSeekerService.class);

    private JobSeekerDAO seekerDAO = new JobSeekerDAO();
    private JobDAO jobDAO = new JobDAO();
    private ApplicationDAO applicationDAO = new ApplicationDAO();
    private ResumeDAO resumeDAO = new ResumeDAO();

    public void start(int userId, String username) {

        Scanner sc = new Scanner(System.in);

        try {
            logger.info(
                    "Job seeker dashboard started | userId={}, username={}",
                    userId, username
            );

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

                logger.info(
                        "Job seeker selected menu option | userId={}, choice={}",
                        userId, choice
                );

                switch (choice) {

                    case 1 -> {
                        logger.info("Opening profile menu | userId={}", userId);
                        profileMenu(userId, sc);
                    }

                    case 2 -> {
                        logger.info("Opening resume menu | userId={}", userId);
                        resumeMenu(userId, sc);
                    }

                    case 3 -> {
                        logger.info("Job seeker searching jobs | userId={}", userId);
                        jobDAO.searchJobs(sc, userId);
                    }

                    case 4 -> {
                        logger.info("Viewing applications | userId={}", userId);
                        applicationDAO.viewMyApplications(userId);
                    }

                    case 5 -> {
                        System.out.print("Application ID: ");
                        int appId = sc.nextInt();
                        logger.info(
                                "Withdrawing application | userId={}, applicationId={}",
                                userId, appId
                        );
                        applicationDAO.withdrawApplication(appId, sc);
                    }

                    case 6 -> {
                        logger.info("Viewing notifications | userId={}", userId);
                        NotificationDAO notificationDAO = new NotificationDAO();
                        notificationDAO.viewNotifications(userId);

                        System.out.print("Mark any notification as read? (yes/no): ");
                        String ans = sc.next();

                        if (ans.equalsIgnoreCase("yes")) {
                            System.out.print("Enter Notification ID: ");
                            int nid = sc.nextInt();
                            logger.info(
                                    "Marking notification as read | userId={}, notificationId={}",
                                    userId, nid
                            );
                            notificationDAO.markAsRead(nid);
                            System.out.println("Notification marked as read.");
                        }
                    }

                    case 7 -> {
                        logger.info("Job seeker logged out | userId={}", userId);
                        System.out.println("Logged out successfully");
                        return;
                    }

                    default -> {
                        logger.warn(
                                "Invalid job seeker menu choice | userId={}, choice={}",
                                userId, choice
                        );
                        System.out.println("Invalid choice");
                    }
                }
            }

        } catch (DatabaseException e) {
            logger.error(
                    "JobSeekerService error | userId={}, message={}",
                    userId, e.getMessage()
            );
            System.out.println(e.getMessage());
        }
    }

    // ================= PROFILE SUB MENU =================
    private void profileMenu(int userId, Scanner sc) {

        try {
            while (true) {

                System.out.println("\n--- PROFILE MENU ---");
                System.out.println("1. Complete Profile");
                System.out.println("2. Update Profile");
                System.out.println("3. Back");

                int ch = sc.nextInt();

                logger.info(
                        "Profile menu option selected | userId={}, choice={}",
                        userId, ch
                );

                switch (ch) {
                    case 1 -> {
                        logger.info("Completing profile | userId={}", userId);
                        seekerDAO.completeProfile(userId, sc);
                    }
                    case 2 -> {
                        logger.info("Updating profile | userId={}", userId);
                        seekerDAO.updateProfile(userId, sc);
                    }
                    case 3 -> {
                        logger.info("Exiting profile menu | userId={}", userId);
                        return;
                    }
                    default -> {
                        logger.warn(
                                "Invalid profile menu choice | userId={}, choice={}",
                                userId, ch
                        );
                        System.out.println("Invalid choice");
                    }
                }
            }

        } catch (DatabaseException e) {
            logger.error(
                    "Profile menu error | userId={}, message={}",
                    userId, e.getMessage()
            );
            System.out.println(e.getMessage());
        }
    }

    // ================= RESUME SUB MENU =================
    private void resumeMenu(int userId, Scanner sc) {

        try {
            while (true) {

                System.out.println("\n--- RESUME MENU ---");
                System.out.println("1. Upload Resume");
                System.out.println("2. Update Resume");
                System.out.println("3. Back");

                int ch = sc.nextInt();

                logger.info(
                        "Resume menu option selected | userId={}, choice={}",
                        userId, ch
                );

                switch (ch) {
                    case 1 -> {
                        logger.info("Uploading resume | userId={}", userId);
                        resumeDAO.uploadResume(userId, sc);
                    }
                    case 2 -> {
                        logger.info("Updating resume | userId={}", userId);
                        resumeDAO.updateResume(userId, sc);
                    }
                    case 3 -> {
                        logger.info("Exiting resume menu | userId={}", userId);
                        return;
                    }
                    default -> {
                        logger.warn(
                                "Invalid resume menu choice | userId={}, choice={}",
                                userId, ch
                        );
                        System.out.println("Invalid choice");
                    }
                }
            }

        } catch (DatabaseException e) {
            logger.error(
                    "Resume menu error | userId={}, message={}",
                    userId, e.getMessage()
            );
            System.out.println(e.getMessage());
        }
    }
}
