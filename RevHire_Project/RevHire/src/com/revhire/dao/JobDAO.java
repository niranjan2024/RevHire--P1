package com.revhire.dao;

import java.sql.*;
import java.util.Scanner;
import com.revhire.config.DBConnection;

public class JobDAO {

    // ================= JOB SEEKER =================

    public void searchJobs(Scanner sc, int userId) {

        sc.nextLine();
        System.out.print("Job Title: ");
        String title = sc.nextLine();

        System.out.print("Location: ");
        String location = sc.nextLine();

        System.out.print("Minimum Experience (years): ");
        int exp = sc.nextInt();
        sc.nextLine();

        System.out.print("Company Name: ");
        String company = sc.nextLine();

        String sql = """
        SELECT j.job_id, j.title, j.location, j.salary_min, j.salary_max, e.company_name
        FROM job_listings j
        JOIN employer_profiles e ON j.employer_id = e.employer_id
        WHERE j.status='OPEN'
        AND j.title LIKE ?
        AND j.location LIKE ?
        AND j.experience_required <= ?
        AND e.company_name LIKE ?
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + title + "%");
            ps.setString(2, "%" + location + "%");
            ps.setInt(3, exp);
            ps.setString(4, "%" + company + "%");

            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- Available Jobs ---");
            while (rs.next()) {
                System.out.println(
                        rs.getInt("job_id") + " | " +
                                rs.getString("title") + " | " +
                                rs.getString("company_name") + " | " +
                                rs.getString("location") + " | " +
                                rs.getDouble("salary_min") + "-" +
                                rs.getDouble("salary_max")
                );
            }

            System.out.print("\nDo you want to apply? (yes/no): ");
            if (sc.next().equalsIgnoreCase("yes")) {
                System.out.print("Enter Job ID: ");
                int jobId = sc.nextInt();
                System.out.print("Enter Resume ID: ");
                int resumeId = sc.nextInt();
                new ApplicationDAO().applyJob(jobId, userId, resumeId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // ================= EMPLOYER =================

    public void postJob(int userId, Scanner sc) {

        int employerId = getEmployerId(userId);

        //  IMPORTANT CHECK
        if (employerId == -1) {
            System.out.println("Employer profile not found.");
            System.out.println("Please complete company profile before posting a job.");
            return;
        }

        sc.nextLine();
        System.out.print("Job Title: ");
        String title = sc.nextLine();

        System.out.print("Description: ");
        String desc = sc.nextLine();

        System.out.print("Skills Required: ");
        String skills = sc.nextLine();

        System.out.print("Experience (years): ");
        int exp = sc.nextInt();

        System.out.print("Salary Min: ");
        double min = sc.nextDouble();

        System.out.print("Salary Max: ");
        double max = sc.nextDouble();

        // 🔴 SALARY VALIDATION
        if (min > max) {
            System.out.println("Salary Min cannot be greater than Salary Max");
            return;
        }

        sc.nextLine();
        System.out.print("Location: ");
        String location = sc.nextLine();

        String sql = """
            INSERT INTO job_listings
            (employer_id, title, description, skills_required,
             experience_required, location, salary_min, salary_max,
             job_type, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'FULL_TIME', 'OPEN')
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, employerId);
            ps.setString(2, title);
            ps.setString(3, desc);
            ps.setString(4, skills);
            ps.setInt(5, exp);
            ps.setString(6, location);
            ps.setDouble(7, min);
            ps.setDouble(8, max);

            ps.executeUpdate();
            System.out.println("Job Posted Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= HELPER =================

    private int getEmployerId(int userId) {

        String sql = "SELECT employer_id FROM employer_profiles WHERE user_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("employer_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // profile not found
    }

    public void reopenJob(int jobId) {
        updateJobStatus(jobId, "OPEN");
    }

    private void updateJobStatus(int jobId, String status) {

        String sql = "UPDATE job_listings SET status=? WHERE job_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, jobId);
            ps.executeUpdate();

            System.out.println("Job status updated to " + status);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewMyJobs(int userId) {

        String sql = """
        SELECT j.job_id, j.title, j.status
        FROM job_listings j
        JOIN employer_profiles e ON j.employer_id = e.employer_id
        WHERE e.user_id = ?
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(
                        rs.getInt("job_id") + " | " +
                                rs.getString("title") + " | " +
                                rs.getString("status")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editJob(int jobId, Scanner sc) {

        sc.nextLine();
        System.out.print("New Title: ");
        String title = sc.nextLine();

        System.out.print("New Location: ");
        String location = sc.nextLine();

        String sql = "UPDATE job_listings SET title=?, location=? WHERE job_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, location);
            ps.setInt(3, jobId);
            ps.executeUpdate();

            System.out.println("Job updated successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeJob(int jobId) {

        String sql = "UPDATE job_listings SET status='CLOSED' WHERE job_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.executeUpdate();
            System.out.println("Job closed successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteJob(int jobId) {

        String sql = "DELETE FROM job_listings WHERE job_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.executeUpdate();
            System.out.println("Job deleted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
