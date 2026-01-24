package com.revhire.Test;

import com.revhire.dao.ResumeDAO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResumeDAOTest {

    @Test
    void resumeDAOObjectCreated() {
        ResumeDAO dao = new ResumeDAO();
        assertNotNull(dao);
    }
}
