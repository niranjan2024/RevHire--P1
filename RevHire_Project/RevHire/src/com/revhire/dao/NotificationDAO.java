package com.revhire.dao;

import java.sql.*;
import com.revhire.config.DBConnection;

public class NotificationDAO {

    public void addNotification(int userId, String message) {

        String sql = "INSERT INTO notifications(user_id, message) VALUES (?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, message);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewNotifications(int userId) {

        String sql = "SELECT message, created_at FROM notifications WHERE user_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- Notifications ---");
            while (rs.next()) {
                System.out.println(
                        rs.getString("message") + " | " +
                                rs.getTimestamp("created_at")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void markAsRead(int notificationId) {

        String sql = """
            UPDATE notifications
            SET is_read = true
            WHERE notification_id = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, notificationId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}