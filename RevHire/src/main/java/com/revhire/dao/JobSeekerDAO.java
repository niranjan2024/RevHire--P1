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

public class JobSeekerDAO {

    private static final Logger logger =
            LogManager.getLogger(JobSeekerDAO.class);

    // ================= COMPLETE PROFILE =================
    public void completeProfile(int userId, Scanner sc) {

        if (profileExists(userId)) {
            logger.warn(
                    "Attempt to complete job seeker profile again | userId={}",
                    userId
            );
            System.out.println("Profile already completed");
            return;
        }

        sc.nextLine();
        System.out.print("Full Name: ");
        String name = sc.nextLine();

        System.out.print("Phone: ");
        String phone = sc.nextLine();

        System.out.print("Location: ");
        String location = sc.nextLine();

        System.out.print("Experience (years): ");
        int exp = sc.nextInt();

        String sql = """
            INSERT INTO job_seeker_profiles
            (user_id, full_name, phone, location, experience_years, profile_complete)
            VALUES (?, ?, ?, ?, ?, true)
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.setString(3, phone);
            ps.setString(4, location);
            ps.setInt(5, exp);

            ps.executeUpdate();

            logger.info(
                    "Job seeker profile completed successfully | userId={}, name={}",
                    userId, name
            );

            System.out.println("Profile completed successfully!");

        } catch (SQLException e) {
            logger.error(
                    "Error completing job seeker profile | userId={}, name={}",
                    userId, name, e
            );
            throw new DatabaseException(
                    "Database error while completing job seeker profile"
            );
        }
    }

    // ================= UPDATE PROFILE =================
    public void updateProfile(int userId, Scanner sc) {

        sc.nextLine();
        System.out.print("New Phone: ");
        String phone = sc.nextLine();

        System.out.print("New Location: ");
        String location = sc.nextLine();

        System.out.print("Experience (years): ");
        int exp = sc.nextInt();

        String sql = """
            UPDATE job_seeker_profiles
            SET phone=?, location=?, experience_years=?
            WHERE user_id=?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, phone);
            ps.setString(2, location);
            ps.setInt(3, exp);
            ps.setInt(4, userId);

            ps.executeUpdate();

            logger.info(
                    "Job seeker profile updated | userId={}, phone={}, location={}",
                    userId, phone, location
            );

            System.out.println("Profile updated successfully!");

        } catch (SQLException e) {
            logger.error(
                    "Error updating job seeker profile | userId={}",
                    userId, e
            );
            throw new DatabaseException(
                    "Database error while updating job seeker profile"
            );
        }
    }

    // ================= HELPER =================
    private boolean profileExists(int userId) {

        String sql =
                "SELECT seeker_id FROM job_seeker_profiles WHERE user_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            boolean exists = rs.next();

            logger.info(
                    "Checked job seeker profile existence | userId={}, exists={}",
                    userId, exists
            );

            return exists;

        } catch (SQLException e) {
            logger.error(
                    "Error checking job seeker profile | userId={}",
                    userId, e
            );
            throw new DatabaseException(
                    "Database error while checking job seeker profile"
            );
        }
    }
}
