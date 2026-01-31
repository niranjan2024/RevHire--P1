package com.revhire.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revhire.config.DBConnection;
import com.revhire.exception.DatabaseException;

public class EducationDAO {

    private static final Logger logger =
            LogManager.getLogger(EducationDAO.class);

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

            logger.info(
                    "Education added successfully | resumeId={}, degree={}, institution={}",
                    resumeId, degree, institution
            );

        } catch (SQLException e) {
            logger.error(
                    "Error adding education | resumeId={}, degree={}, institution={}",
                    resumeId, degree, institution, e
            );
            throw new DatabaseException(
                    "Database error while adding education details"
            );
        }
    }
}
