package hu.hirannor.hexagonal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@DisplayName("HexagonalApplication")
@ActiveProfiles("test")
class HexagonalApplicationIntegrationTest {

    @Test
    @DisplayName("Should load context")
    void contextLoads() {
    }

}
