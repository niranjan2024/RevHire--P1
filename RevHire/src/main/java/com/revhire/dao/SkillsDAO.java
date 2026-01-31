package com.revhire.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revhire.config.DBConnection;
import com.revhire.exception.DatabaseException;

public class SkillsDAO {

    private static final Logger logger =
            LogManager.getLogger(SkillsDAO.class);

    public void addSkill(int resumeId, String skillName, String proficiency) {

        String sql = """
            INSERT INTO skills (resume_id, skill_name, proficiency)
            VALUES (?, ?, ?)
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, resumeId);
            ps.setString(2, skillName.trim());
            ps.setString(3, proficiency);

            ps.executeUpdate();

            logger.info(
                    "Skill added successfully | resumeId={}, skill={}, proficiency={}",
                    resumeId, skillName.trim(), proficiency
            );

        } catch (SQLException e) {
            logger.error(
                    "Error adding skill | resumeId={}, skill={}",
                    resumeId, skillName, e
            );
            throw new DatabaseException("Database error while adding skill");
        }
    }
}
