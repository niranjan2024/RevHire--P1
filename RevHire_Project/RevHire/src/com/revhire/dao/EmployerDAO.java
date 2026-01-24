package com.revhire.dao;

import java.sql.*;
import java.util.Scanner;
import com.revhire.config.DBConnection;

public class EmployerDAO {
    public boolean profileExists(int userId) {

        String sql = "SELECT employer_id FROM employer_profiles WHERE user_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            return rs.next();   // true if profile exists

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void completeCompanyProfile(int userId, Scanner sc) {

        if (profileExists(userId)) {
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
            System.out.println("Company profile completed successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
