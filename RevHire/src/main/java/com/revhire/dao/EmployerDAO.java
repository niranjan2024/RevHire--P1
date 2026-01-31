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

public class EmployerDAO {

    private static final Logger logger =
            LogManager.getLogger(EmployerDAO.class);

    // ================= CHECK PROFILE EXISTS =================
    public boolean profileExists(int userId) {

        String sql =
                "SELECT employer_id FROM employer_profiles WHERE user_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            boolean exists = rs.next();
            logger.info(
                    "Checked employer profile existence | userId={}, exists={}",
                    userId, exists
            );

            return exists;

        } catch (SQLException e) {
            logger.error(
                    "Error checking employer profile | userId={}",
                    userId, e
            );
            throw new DatabaseException(
                    "Database error while checking employer profile"
            );
        }
    }

    // ================= COMPLETE COMPANY PROFILE =================
    public void completeCompanyProfile(int userId, Scanner sc) {

        if (profileExists(userId)) {
            logger.warn(
                    "Attempt to complete employer profile again | userId={}",
                    userId
            );
            System.out.println("Company profile already completed");
            return;
        }

        sc.nextLine();
        System.out.print("Company Name: ");
        String company = sc.nextLine();

        System.out.print("Industry: ");
        String industry = sc.nextLine();

        System.out.print("Company Size: ");
        int size = sc.nextInt();
        sc.nextLine();

        System.out.print("Website: ");
        String website = sc.nextLine();

        System.out.print("Location: ");
        String location = sc.nextLine();

        String sql = """
            INSERT INTO employer_profiles
            (user_id, company_name, industry, company_size, website, location)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, company);
            ps.setString(3, industry);
            ps.setInt(4, size);
            ps.setString(5, website);
            ps.setString(6, location);

            ps.executeUpdate();

            logger.info(
                    "Employer profile completed successfully | userId={}, company={}",
                    userId, company
            );

            System.out.println("Company profile completed successfully!");

        } catch (SQLException e) {
            logger.error(
                    "Error completing employer profile | userId={}, company={}",
                    userId, company, e
            );
            throw new DatabaseException(
                    "Database error while completing employer profile"
            );
        }
    }
}
