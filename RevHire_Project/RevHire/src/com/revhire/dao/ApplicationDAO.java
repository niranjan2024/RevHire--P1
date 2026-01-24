package com.revhire.dao;

import java.sql.*;
import java.util.Scanner;
import com.revhire.config.DBConnection;

public class ApplicationDAO {

    // ================= JOB SEEKER =================

    public void applyJob(int jobId, int userId, int resumeId) {

        int seekerId = getSeekerId(userId);
        if (seekerId == -1) {
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
            System.out.println("Applied Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
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

            while (rs.next()) {
                System.out.println(
                        rs.getInt("application_id") + " | " +
                                rs.getString("title") + " | " +
                                rs.getString("status") + " | " +
                                rs.getTimestamp("applied_date")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
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

            while (rs.next()) {
                System.out.println(
                        rs.getInt("application_id") + " | " +
                                rs.getString("title") + " | " +
                                rs.getString("full_name") + " | " +
                                rs.getString("status")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
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

            // 1️⃣ Update application status
            try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                ps.setString(1, status);
                ps.setInt(2, appId);
                ps.executeUpdate();
            }

            // 2️⃣ Get job seeker user_id & job title
            int userId = -1;
            String jobTitle = "";

            try (PreparedStatement ps = con.prepareStatement(fetchSql)) {
                ps.setInt(1, appId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    userId = rs.getInt("user_id");
                    jobTitle = rs.getString("title");
                }
            }

            // 3️⃣ Send notification
            if (userId != -1) {
                String message =
                        status.equalsIgnoreCase("Shortlisted")
                                ? "Congratulations! Your application for '" + jobTitle + "' has been shortlisted."
                                : "We regret to inform you that your application for '" + jobTitle + "' has been rejected.";

                new NotificationDAO().addNotification(userId, message);
            }

            System.out.println("Application " + status);

        } catch (Exception e) {
            e.printStackTrace();
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

            System.out.println("Application withdrawn successfully!");

        } catch (Exception e) {
            e.printStackTrace();
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }
}
