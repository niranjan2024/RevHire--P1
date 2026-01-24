package com.revhire.Test;

import com.revhire.dao.UserDAO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    @Test
    void userDAOObjectCreated() {
        UserDAO dao = new UserDAO();
        assertNotNull(dao);
    }

    @Test
    void registerMethodExists() {
        UserDAO dao = new UserDAO();
        assertDoesNotThrow(() -> {
            // Just checking method exists, not DB
            dao.getClass().getMethod(
                    "register",
                    String.class,
                    String.class,
                    String.class,
                    String.class,
                    String.class,
                    String.class
            );
        });
    }
}
