package com.hirannor.hexagonal.adapter.persistence.jpa;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.persistence",
        havingValue = "spring-data-jpa"
)
public class JpaPersistenceConfiguration {

    private static final String BUNDLE_INIT_PATH =
        "classpath:adapter/persistence/bundle-init.xml";

    private final DataSource ds;

    @Autowired
    JpaPersistenceConfiguration(final DataSource ds) {
        this.ds = ds;
    }

    @Bean("liquibaseForPersistence")
    SpringLiquibase initializeDatabaseMigration() {
        final SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(ds);
        liquibase.setChangeLog(BUNDLE_INIT_PATH);

        return liquibase;
    }

}
