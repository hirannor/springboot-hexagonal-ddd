package hu.hirannor.hexagonal;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public class TestContainerBase {

    private static final PostgreSQLContainer POSTGRE_SQL_CONTAINER;

    static {
        POSTGRE_SQL_CONTAINER =
                new PostgreSQLContainer<>("postgres:14").withReuse(true);

        POSTGRE_SQL_CONTAINER.start();
    }

    public TestContainerBase() {}

    @DynamicPropertySource
    private static void properties(final DynamicPropertyRegistry registry) {
        registry.add(
                "spring.datasource.url",
                POSTGRE_SQL_CONTAINER::getJdbcUrl
        );
        registry.add(
                "spring.datasource.username",
                POSTGRE_SQL_CONTAINER::getUsername
        );
        registry.add(
                "spring.datasource.password",
                POSTGRE_SQL_CONTAINER::getPassword
        );
    }
}
