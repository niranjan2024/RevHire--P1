package com.revhire.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revhire.config.DBConnection;
import com.revhire.exception.DatabaseException;

public class ExperienceDAO {

    private static final Logger logger =
            LogManager.getLogger(ExperienceDAO.class);

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

            logger.info(
                    "Experience added successfully | resumeId={}, jobTitle={}, company={}",
                    resumeId, jobTitle, company
            );

        } catch (SQLException e) {
            logger.error(
                    "Error adding experience | resumeId={}, jobTitle={}, company={}",
                    resumeId, jobTitle, company, e
            );
            throw new DatabaseException(
                    "Database error while adding experience details"
            );
        }
    }
}
