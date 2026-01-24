package com.revhire.dao;

import java.sql.*;
import com.revhire.config.DBConnection;

public class ExperienceDAO {

    public void addExperience(int resumeId,
                              String jobTitle,
                              String company,
                              String startDate,
                              String endDate) {

        String sql = """
            INSERT INTO experience
            (resume_id, job_title, company, start_date, end_date)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, resumeId);
            ps.setString(2, jobTitle);
            ps.setString(3, company);
            ps.setDate(4, Date.valueOf(startDate));
            ps.setDate(5, Date.valueOf(endDate));

            ps.executeUpdate();
            System.out.println("Experience details added successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
