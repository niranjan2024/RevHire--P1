package com.revhire.dao;

import java.sql.*;
import com.revhire.config.DBConnection;
import com.revhire.model.User;
import com.revhire.util.PasswordUtil;

public class UserDAO {
    public int register(String username, String email, String password,
                        String role, String question, String answer) {

        String sql = """
        INSERT INTO users
        (username, email, password, role, security_question, security_answer)
        VALUES (?,?,?,?,?,?)
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, PasswordUtil.hashPassword(password));
            ps.setString(4, role);
            ps.setString(5, question);
            ps.setString(6, answer);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    public User login(String email, String password) {
        String sql = "SELECT * FROM users WHERE email=? AND password=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, PasswordUtil.hashPassword(password));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("role"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public void changePassword(int userId, String oldPass, String newPass) {

        String sql = """
        UPDATE users
        SET password=?
        WHERE user_id=? AND password=?
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newPass);
            ps.setInt(2, userId);
            ps.setString(3, oldPass);

            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Password changed" : "Invalid current password");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean resetPassword(String email, String answer, String newPass) {

        String sql = """
        UPDATE users
        SET password=?
        WHERE email=? AND security_answer=?
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newPass);
            ps.setString(2, email);
            ps.setString(3, answer);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean changePassword(String email,
                                  String oldPassword,
                                  String newPassword) {

        String checkSql =
                "SELECT user_id FROM users WHERE email=? AND password=?";
        String updateSql =
                "UPDATE users SET password=? WHERE email=?";

        try (Connection con = DBConnection.getConnection()) {

            // 1️ Verify current password
            try (PreparedStatement ps = con.prepareStatement(checkSql)) {
                ps.setString(1, email);
                ps.setString(2, oldPassword);
                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    return false; // invalid old password
                }
            }

            // 2️ Update new password
            try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                ps.setString(1, newPassword);
                ps.setString(2, email);
                ps.executeUpdate();
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }



}