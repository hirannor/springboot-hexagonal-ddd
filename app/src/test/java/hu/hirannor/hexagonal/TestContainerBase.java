package hu.hirannor.hexagonal;

import org.testcontainers.containers.PostgreSQLContainer;

public class TestContainerBase {

    private static final PostgreSQLContainer POSTGRE_SQL_CONTAINER;

    static {
        POSTGRE_SQL_CONTAINER =
                new PostgreSQLContainer<>("postgres:14")
                        .withReuse(true)
                        .withDatabaseName("test-db")
                        .withUsername("sa")
                        .withPassword("sa");

        POSTGRE_SQL_CONTAINER.start();
    }

    public TestContainerBase() {
    }
}
