package com.revhire.service;

import java.util.Scanner;
import com.revhire.dao.*;

public class EmployerService {

    JobDAO jobDAO = new JobDAO();
    ApplicationDAO appDAO = new ApplicationDAO();

    public void start(int userId) {

        Scanner sc = new Scanner(System.in);
        EmployerDAO employerDAO = new EmployerDAO();
        JobDAO jobDAO = new JobDAO();
        ApplicationDAO appDAO = new ApplicationDAO();

        // 🔥 CHECK PROFILE FIRST
        if (!employerDAO.profileExists(userId)) {
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

            switch (ch) {
                case 1 -> jobDAO.postJob(userId, sc);
                case 2 -> jobDAO.viewMyJobs(userId);
                case 3 -> {
                    System.out.print("Job ID: ");
                    jobDAO.editJob(sc.nextInt(), sc);
                }
                case 4 -> {
                    System.out.print("Job ID: ");
                    jobDAO.closeJob(sc.nextInt());
                }

                case 5 -> appDAO.viewApplicantsForEmployer(userId);
                case 6 -> {
                    System.out.print("Application ID: ");
                    appDAO.shortlistApplication(sc.nextInt());
                }
                case 7 -> {
                    System.out.print("Application ID: ");
                    appDAO.rejectApplication(sc.nextInt());
                }
                case 8 -> { return; }
            }
        }
    }

}
