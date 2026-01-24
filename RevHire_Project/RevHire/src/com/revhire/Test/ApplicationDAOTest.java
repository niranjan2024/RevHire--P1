package com.revhire.Test;

import com.revhire.dao.ApplicationDAO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationDAOTest {

    @Test
    void applicationDAOObjectCreated() {
        ApplicationDAO dao = new ApplicationDAO();
        assertNotNull(dao);
    }
}
