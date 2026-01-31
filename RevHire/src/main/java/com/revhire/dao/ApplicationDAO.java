package com.revhire.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revhire.config.DBConnection;
import com.revhire.exception.DatabaseException;

public class ApplicationDAO {

    private static final Logger logger =
            LogManager.getLogger(ApplicationDAO.class);

    // ================= JOB SEEKER =================
    public void applyJob(int jobId, int userId, int resumeId) {

        int seekerId = getSeekerId(userId);
        if (seekerId == -1) {
            logger.warn("Apply job failed: profile not completed for userId={}", userId);
            System.out.println("Please complete your profile before applying.");
            return;
        }

        String sql = """
            INSERT INTO applications(job_id, seeker_id, resume_id, status)
            VALUES (?, ?, ?, 'Applied')
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.setInt(2, seekerId);
            ps.setInt(3, resumeId);
            ps.executeUpdate();

            logger.info("Job application submitted | jobId={}, seekerId={}, resumeId={}",
                    jobId, seekerId, resumeId);

            System.out.println("Applied Successfully!");

        } catch (SQLException e) {
            logger.error("Error applying for job | jobId={}, userId={}", jobId, userId, e);
            throw new DatabaseException("Database error while applying for job");
        }
    }

    public void viewMyApplications(int userId) {

        String sql = """
            SELECT a.application_id, j.title, a.status, a.applied_date
            FROM applications a
            JOIN job_listings j ON a.job_id = j.job_id
            JOIN job_seeker_profiles p ON a.seeker_id = p.seeker_id
            WHERE p.user_id = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            logger.info("Fetching applications for userId={}", userId);

            while (rs.next()) {
                System.out.println(
                        rs.getInt("application_id") + " | " +
                                rs.getString("title") + " | " +
                                rs.getString("status") + " | " +
                                rs.getTimestamp("applied_date")
                );
            }

        } catch (SQLException e) {
            logger.error("Error fetching applications for userId={}", userId, e);
            throw new DatabaseException("Database error while fetching applications");
        }
    }

    // ================= EMPLOYER =================
    public void viewApplicantsForEmployer(int userId) {

        String sql = """
            SELECT a.application_id, j.title, p.full_name, a.status
            FROM applications a
            JOIN job_listings j ON a.job_id = j.job_id
            JOIN job_seeker_profiles p ON a.seeker_id = p.seeker_id
            JOIN employer_profiles e ON j.employer_id = e.employer_id
            WHERE e.user_id = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            logger.info("Employer viewing applicants | employerUserId={}", userId);

            while (rs.next()) {
                System.out.println(
                        rs.getInt("application_id") + " | " +
                                rs.getString("title") + " | " +
                                rs.getString("full_name") + " | " +
                                rs.getString("status")
                );
            }

        } catch (SQLException e) {
            logger.error("Error fetching applicants for employer userId={}", userId, e);
            throw new DatabaseException("Database error while fetching applicants");
        }
    }

    public void shortlistApplication(int applicationId) {
        updateStatus(applicationId, "Shortlisted");
    }

    public void rejectApplication(int applicationId) {
        updateStatus(applicationId, "Rejected");
    }

    // ================= UPDATE STATUS + NOTIFICATION =================
    private void updateStatus(int appId, String status) {

        String updateSql =
                "UPDATE applications SET status=? WHERE application_id=?";

        String fetchSql = """
            SELECT u.user_id, j.title
            FROM applications a
            JOIN job_seeker_profiles p ON a.seeker_id = p.seeker_id
            JOIN users u ON p.user_id = u.user_id
            JOIN job_listings j ON a.job_id = j.job_id
            WHERE a.application_id = ?
        """;

        try (Connection con = DBConnection.getConnection()) {

            // 1️ Update status
            try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                ps.setString(1, status);
                ps.setInt(2, appId);
                ps.executeUpdate();
            }

            int userId = -1;
            String jobTitle = "";

            // 2️ Fetch user + job
            try (PreparedStatement ps = con.prepareStatement(fetchSql)) {
                ps.setInt(1, appId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    userId = rs.getInt("user_id");
                    jobTitle = rs.getString("title");
                }
            }

            // 3️ Notify
            if (userId != -1) {
                String message =
                        status.equalsIgnoreCase("Shortlisted")
                                ? "Congratulations! Your application for '" + jobTitle + "' has been shortlisted."
                                : "We regret to inform you that your application for '" + jobTitle + "' has been rejected.";

                new NotificationDAO().addNotification(userId, message);
            }

            logger.info("Application status updated | applicationId={}, status={}", appId, status);
            System.out.println("Application " + status);

        } catch (SQLException e) {
            logger.error("Error updating application status | applicationId={}, status={}",
                    appId, status, e);
            throw new DatabaseException("Database error while updating application status");
        }
    }

    // ================= WITHDRAW APPLICATION =================
    public void withdrawApplication(int applicationId, Scanner sc) {

        sc.nextLine();
        System.out.print("Reason for withdrawal (optional): ");
        String reason = sc.nextLine();

        String sql = """
            UPDATE applications
            SET status='Withdrawn', withdraw_reason=?
            WHERE application_id=?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, reason);
            ps.setInt(2, applicationId);
            ps.executeUpdate();

            logger.info("Application withdrawn | applicationId={}", applicationId);
            System.out.println("Application withdrawn successfully!");

        } catch (SQLException e) {
            logger.error("Error withdrawing application | applicationId={}", applicationId, e);
            throw new DatabaseException("Database error while withdrawing application");
        }
    }

    // ================= HELPER =================
    private int getSeekerId(int userId) {

        String sql =
                "SELECT seeker_id FROM job_seeker_profiles WHERE user_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("seeker_id");
            }

            return -1;

        } catch (SQLException e) {
            logger.error("Error fetching seekerId for userId={}", userId, e);
            throw new DatabaseException("Database error while fetching seeker profile");
        }
    }
}
