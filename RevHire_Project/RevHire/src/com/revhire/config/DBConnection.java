package com.revhire.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/revhire_db";
    private static final String USER = "root";
    private static final String PASS = "MySql@1608";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(URL, USER, PASS);

        } catch (Exception e) {
            throw new RuntimeException("Database connection failed", e);
        }
    }
}
