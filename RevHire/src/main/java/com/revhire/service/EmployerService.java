package com.revhire.service;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revhire.dao.ApplicationDAO;
import com.revhire.dao.EmployerDAO;
import com.revhire.dao.JobDAO;
import com.revhire.exception.DatabaseException;

public class EmployerService {

    private static final Logger logger =
            LogManager.getLogger(EmployerService.class);

    private JobDAO jobDAO = new JobDAO();
    private ApplicationDAO appDAO = new ApplicationDAO();
    private EmployerDAO employerDAO = new EmployerDAO();

    public void start(int userId) {

        Scanner sc = new Scanner(System.in);

        try {
            logger.info("Employer dashboard started | userId={}", userId);

            //  CHECK PROFILE FIRST
            if (!employerDAO.profileExists(userId)) {
                logger.warn(
                        "Employer profile not completed | userId={}",
                        userId
                );
                System.out.println("Complete company profile before posting jobs.");
                employerDAO.completeCompanyProfile(userId, sc);
            }

            while (true) {

                System.out.println("\n=== EMPLOYER DASHBOARD ===");
                System.out.println("1. Post Job");
                System.out.println("2. View My Jobs");
                System.out.println("3. Edit Job");
                System.out.println("4. Close Job");
                System.out.println("5. View Applicants");
                System.out.println("6. Shortlist Applicant");
                System.out.println("7. Reject Applicant");
                System.out.println("8. Logout");

                int ch = sc.nextInt();

                logger.info("Employer selected menu option | userId={}, choice={}", userId, ch);

                switch (ch) {
                    case 1 -> {
                        logger.info("Employer posting job | userId={}", userId);
                        jobDAO.postJob(userId, sc);
                    }
                    case 2 -> {
                        logger.info("Employer viewing jobs | userId={}", userId);
                        jobDAO.viewMyJobs(userId);
                    }
                    case 3 -> {
                        System.out.print("Job ID: ");
                        int jobId = sc.nextInt();
                        logger.info("Employer editing job | userId={}, jobId={}", userId, jobId);
                        jobDAO.editJob(jobId, sc);
                    }
                    case 4 -> {
                        System.out.print("Job ID: ");
                        int jobId = sc.nextInt();
                        logger.info("Employer closing job | userId={}, jobId={}", userId, jobId);
                        jobDAO.closeJob(jobId);
                    }
                    case 5 -> {
                        logger.info("Employer viewing applicants | userId={}", userId);
                        appDAO.viewApplicantsForEmployer(userId);
                    }
                    case 6 -> {
                        System.out.print("Application ID: ");
                        int appId = sc.nextInt();
                        logger.info(
                                "Employer shortlisting application | userId={}, applicationId={}",
                                userId, appId
                        );
                        appDAO.shortlistApplication(appId);
                    }
                    case 7 -> {
                        System.out.print("Application ID: ");
                        int appId = sc.nextInt();
                        logger.info(
                                "Employer rejecting application | userId={}, applicationId={}",
                                userId, appId
                        );
                        appDAO.rejectApplication(appId);
                    }
                    case 8 -> {
                        logger.info("Employer logged out | userId={}", userId);
                        return;
                    }
                    default -> {
                        logger.warn(
                                "Invalid employer menu choice | userId={}, choice={}",
                                userId, ch
                        );
                        System.out.println("Invalid choice");
                    }
                }
            }

        } catch (DatabaseException e) {
            logger.error(
                    "Employer service error | userId={}, message={}",
                    userId, e.getMessage()
            );
            System.out.println(e.getMessage());
        }
    }
}
