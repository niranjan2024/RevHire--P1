package com.revhire.dao;

import java.sql.*;
import java.util.Scanner;
import com.revhire.config.DBConnection;

public class ResumeDAO {

    // ================= UPLOAD RESUME =================
    public int uploadResume(int userId, Scanner sc) {

        int seekerId = getSeekerId(userId);
        if (seekerId == -1) {
            System.out.println("Please complete your profile before uploading resume.");
            return -1;
        }

        sc.nextLine(); // clear buffer

        System.out.print("Career Objective: ");
        String objective = sc.nextLine();

        // ===== EDUCATION DETAILS =====
        System.out.println("=== Education Details ===");

        System.out.print("Degree: ");
        String degree = sc.nextLine();

        System.out.print("Institution: ");
        String institution = sc.nextLine();

        System.out.print("Start Date (YYYY-MM-DD): ");
        String startDate = sc.next();

        System.out.print("End Date (YYYY-MM-DD): ");
        String endDate = sc.next();
        sc.nextLine();

        String educationSummary =
                degree + ", " + institution + ", " + endDate.substring(0, 4);

        // ===== EXPERIENCE DETAILS =====
        System.out.println("=== Experience Details ===");

        System.out.print("Job Title: ");
        String jobTitle = sc.nextLine();

        System.out.print("Company Name: ");
        String company = sc.nextLine();

        System.out.print("Experience Start Date (YYYY-MM-DD): ");
        String expStart = sc.next();

        System.out.print("Experience End Date (YYYY-MM-DD): ");
        String expEnd = sc.next();
        sc.nextLine();

        // Experience summary for resumes table
        String experienceSummary = jobTitle + " at " + company;

        System.out.print("Projects: ");
        String projects = sc.nextLine();

        System.out.print("Skills (comma separated): ");
        String skills = sc.nextLine();

        String sql = """
            INSERT INTO resumes
            (seeker_id, education, objective, experience, projects, skills)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, seekerId);
            ps.setString(2, educationSummary);
            ps.setString(3, objective);
            ps.setString(4, experienceSummary);
            ps.setString(5, projects);
            ps.setString(6, skills);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int resumeId = rs.getInt(1);
                System.out.println("Resume uploaded successfully!");

                // 🔥 Store detailed education
                new EducationDAO().addEducation(
                        resumeId, degree, institution, startDate, endDate
                );

                // 🔥 Store detailed experience
                new ExperienceDAO().addExperience(
                        resumeId, jobTitle, company, expStart, expEnd
                );

                // 🔥 Store skills in skills table
                SkillsDAO skillsDAO = new SkillsDAO();
                String[] skillArray = skills.split(",");

                for (String skill : skillArray) {
                    skillsDAO.addSkill(resumeId, skill, "Intermediate");
                }

                System.out.println("Skills added successfully!");

                return resumeId;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    // ================= UPDATE RESUME =================
    public void updateResume(int userId, Scanner sc) {

        int seekerId = getSeekerId(userId);
        if (seekerId == -1) {
            System.out.println("Please complete profile first.");
            return;
        }

        sc.nextLine();
        System.out.print("Resume ID: ");
        int resumeId = sc.nextInt();
        sc.nextLine();

        System.out.print("Updated Career Objective: ");
        String objective = sc.nextLine();

        System.out.print("Updated Experience Summary: ");
        String experience = sc.nextLine();

        System.out.print("Updated Projects: ");
        String projects = sc.nextLine();

        System.out.print("Updated Skills: ");
        String skills = sc.nextLine();

        String sql = """
            UPDATE resumes
            SET objective=?, experience=?, projects=?, skills=?
            WHERE resume_id=? AND seeker_id=?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, objective);
            ps.setString(2, experience);
            ps.setString(3, projects);
            ps.setString(4, skills);
            ps.setInt(5, resumeId);
            ps.setInt(6, seekerId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Resume updated successfully!");
            } else {
                System.out.println("Resume not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= HELPER =================
    private int getSeekerId(int userId) {

        String sql = "SELECT seeker_id FROM job_seeker_profiles WHERE user_id=?";

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
