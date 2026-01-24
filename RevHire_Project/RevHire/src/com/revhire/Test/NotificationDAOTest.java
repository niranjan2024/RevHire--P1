package com.revhire.Test;

import com.revhire.dao.NotificationDAO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class NotificationDAOTest {

    @Test
    void notificationDAOObjectCreationTest() {
        NotificationDAO dao = new NotificationDAO();
        assertNotNull(dao);
    }
}
