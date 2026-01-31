package com.revhire.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revhire.config.DBConnection;
import com.revhire.exception.DatabaseException;

public class NotificationDAO {

    private static final Logger logger =
            LogManager.getLogger(NotificationDAO.class);

    // ================= ADD NOTIFICATION =================
    public void addNotification(int userId, String message) {

        String sql =
                "INSERT INTO notifications(user_id, message) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, message);
            ps.executeUpdate();

            logger.info(
                    "Notification added | userId={}, messageLength={}",
                    userId, message.length()
            );

        } catch (SQLException e) {
            logger.error(
                    "Error adding notification | userId={}",
                    userId, e
            );
            throw new DatabaseException(
                    "Database error while adding notification"
            );
        }
    }

    // ================= VIEW NOTIFICATIONS =================
    public void viewNotifications(int userId) {

        String sql =
                "SELECT message, created_at FROM notifications WHERE user_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            logger.info("Fetching notifications | userId={}", userId);

            System.out.println("\n--- Notifications ---");
            while (rs.next()) {
                System.out.println(
                        rs.getString("message") + " | " +
                                rs.getTimestamp("created_at")
                );
            }

        } catch (SQLException e) {
            logger.error(
                    "Error fetching notifications | userId={}",
                    userId, e
            );
            throw new DatabaseException(
                    "Database error while fetching notifications"
            );
        }
    }

    // ================= MARK AS READ =================
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

            logger.info(
                    "Notification marked as read | notificationId={}",
                    notificationId
            );

        } catch (SQLException e) {
            logger.error(
                    "Error marking notification as read | notificationId={}",
                    notificationId, e
            );
            throw new DatabaseException(
                    "Database error while updating notification status"
            );
        }
    }
}
