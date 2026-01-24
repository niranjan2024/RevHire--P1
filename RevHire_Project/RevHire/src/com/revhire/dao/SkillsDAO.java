package com.revhire.dao;

import java.sql.*;
import com.revhire.config.DBConnection;

public class SkillsDAO {

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

