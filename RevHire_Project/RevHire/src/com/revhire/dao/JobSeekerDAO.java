package com.revhire.dao;
import java.sql.*;
import java.util.Scanner;
import com.revhire.config.DBConnection;

public class JobSeekerDAO {

    public void completeProfile(int userId, Scanner sc) {

        if (profileExists(userId)) {
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

            System.out.println("Profile completed successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean profileExists(int userId) {
        String sql = "SELECT seeker_id FROM job_seeker_profiles WHERE user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeQuery().next();
        } catch (Exception e) {
            return false;
        }
    }
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
            System.out.println("Profile updated successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

