package Test;

import com.revhire.dao.JobDAO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class JobDAOTest {

    @Test
    void jobDAOObjectCreationTest() {
        JobDAO dao = new JobDAO();
        assertNotNull(dao);
    }
}
