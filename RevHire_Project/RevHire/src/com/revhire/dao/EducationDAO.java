package com.revhire.dao;

import java.sql.*;
import com.revhire.config.DBConnection;

public class EducationDAO {

    public void addEducation(int resumeId,
                             String degree,
                             String institution,
                             String startDate,
                             String endDate) {

        String sql = """
            INSERT INTO education
            (resume_id, degree, institution, start_date, end_date)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, resumeId);
            ps.setString(2, degree);
            ps.setString(3, institution);
            ps.setDate(4, Date.valueOf(startDate));
            ps.setDate(5, Date.valueOf(endDate));

            ps.executeUpdate();
            System.out.println("Education details added successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
