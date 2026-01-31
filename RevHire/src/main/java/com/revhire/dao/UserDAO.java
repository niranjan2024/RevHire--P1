package com.revhire.dao;

import java.sql.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revhire.config.DBConnection;
import com.revhire.exception.DatabaseException;
import com.revhire.model.User;
import com.revhire.util.PasswordUtil;

public class UserDAO {

    private static final Logger logger =
            LogManager.getLogger(UserDAO.class);

    // ================= REGISTER =================
    public int register(String username, String email, String password,
                        String role, String question, String answer) {

        String sql = """
            INSERT INTO users
            (username, email, password, role, security_question, security_answer)
            VALUES (?,?,?,?,?,?)
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, PasswordUtil.hashPassword(password));
            ps.setString(4, role);
            ps.setString(5, question);
            ps.setString(6, answer);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int userId = rs.getInt(1);

                logger.info(
                        "User registered successfully | userId={}, email={}, role={}",
                        userId, email, role
                );

                return userId;
            }

            return -1;

        } catch (SQLException e) {
            logger.error(
                    "Error registering user | email={}, role={}",
                    email, role, e
            );
            throw new DatabaseException("Database error while registering user");
        }
    }

    // ================= LOGIN =================
    public User login(String email, String password) {

        String sql = "SELECT * FROM users WHERE email=? AND password=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, PasswordUtil.hashPassword(password));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("role")
                );

                logger.info(
                        "User login successful | userId={}, email={}",
                        user.userId, email
                );

                return user;
            }

            logger.warn(
                    "Invalid login attempt | email={}",
                    email
            );

            return null;

        } catch (SQLException e) {
            logger.error(
                    "Error during login | email={}",
                    email, e
            );
            throw new DatabaseException("Database error while logging in user");
        }
    }

    // ================= CHANGE PASSWORD (BY USER ID) =================
    public boolean changePassword(int userId,
                                  String oldPass,
                                  String newPass) {

        String sql = """
            UPDATE users
            SET password=?
            WHERE user_id=? AND password=?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, PasswordUtil.hashPassword(newPass));
            ps.setInt(2, userId);
            ps.setString(3, PasswordUtil.hashPassword(oldPass));

            boolean updated = ps.executeUpdate() > 0;

            if (updated) {
                logger.info(
                        "Password changed successfully | userId={}",
                        userId
                );
            } else {
                logger.warn(
                        "Password change failed (invalid old password) | userId={}",
                        userId
                );
            }

            return updated;

        } catch (SQLException e) {
            logger.error(
                    "Error changing password | userId={}",
                    userId, e
            );
            throw new DatabaseException("Database error while changing password");
        }
    }

    // ================= RESET PASSWORD =================
    public boolean resetPassword(String email,
                                 String answer,
                                 String newPass) {

        String sql = """
            UPDATE users
            SET password=?
            WHERE email=? AND security_answer=?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, PasswordUtil.hashPassword(newPass));
            ps.setString(2, email);
            ps.setString(3, answer);

            boolean updated = ps.executeUpdate() > 0;

            if (updated) {
                logger.info(
                        "Password reset successful | email={}",
                        email
                );
            } else {
                logger.warn(
                        "Password reset failed (invalid answer) | email={}",
                        email
                );
            }

            return updated;

        } catch (SQLException e) {
            logger.error(
                    "Error resetting password | email={}",
                    email, e
            );
            throw new DatabaseException("Database error while resetting password");
        }
    }

    // ================= CHANGE PASSWORD (BY EMAIL) =================
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
                ps.setString(2, PasswordUtil.hashPassword(oldPassword));

                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    logger.warn(
                            "Password change failed (invalid current password) | email={}",
                            email
                    );
                    return false;
                }
            }

            // 2️ Update password
            try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                ps.setString(1, PasswordUtil.hashPassword(newPassword));
                ps.setString(2, email);
                ps.executeUpdate();
            }

            logger.info(
                    "Password changed successfully | email={}",
                    email
            );

            return true;

        } catch (SQLException e) {
            logger.error(
                    "Error updating password | email={}",
                    email, e
            );
            throw new DatabaseException("Database error while updating password");
        }
    }
}
