package com.revhire.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBConnection {

    private static final Logger logger =
            LogManager.getLogger(DBConnection.class);

    private static final String URL =
            "jdbc:mysql://localhost:3306/revhire_db";
    private static final String USER = "root";
    private static final String PASS = "MySql@1608";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            logger.info("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            logger.fatal("MySQL JDBC Driver not found", e);
            throw new RuntimeException("JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() {
        try {
            logger.debug("Attempting database connection to {}", URL);
            return DriverManager.getConnection(URL, USER, PASS);

        } catch (SQLException e) {
            logger.error("Failed to establish database connection", e);
            throw new RuntimeException("Database connection failed", e);
        }
    }
}
