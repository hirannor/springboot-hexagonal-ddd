package io.github.hirannor.oms.adapter.persistence.jpa;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Spring configuration class for spring data jpa adapter.
 *
 * @author Mate Karolyi
 */
@Configuration
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.persistence",
        havingValue = "spring-data-jpa"
)
@EnableConfigurationProperties({org.springframework.boot.autoconfigure.orm.jpa.JpaProperties.class, DataSourceProperties.class})
public class JpaPersistenceConfiguration {

    private static final String BUNDLE_INIT_PATH =
            "classpath:adapter/persistence/bundle-init.xml";

    final DataSourceProperties dataSourceProperties;
    final JpaProperties jpaProperties;

    @Autowired
    JpaPersistenceConfiguration(final DataSourceProperties dataSourceProperties,
                                final JpaProperties jpaProperties) {
        this.dataSourceProperties = dataSourceProperties;
        this.jpaProperties = jpaProperties;
    }

    @Bean
    DataSource createDataSource(final DataSourceProperties dataSourceProperties) {
        return dataSourceProperties
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean("liquibaseForPersistence")
    SpringLiquibase initializeDatabaseMigration(final DataSource ds) {
        final SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(ds);
        liquibase.setChangeLog(BUNDLE_INIT_PATH);

        return liquibase;
    }

    @Bean(name = "entityManagerFactory")
    LocalContainerEntityManagerFactoryBean createEntityManagerFactory(
            final DataSource ds,
            final JpaProperties jpaProperties) {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(ds);
        em.setPackagesToScan(JpaPersistenceConfiguration.class.getPackage().getName());

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(jpaProperties.isShowSql());

        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaPropertyMap(jpaProperties.getProperties());
        return em;
    }

    @Bean(name = "transactionManager")
    PlatformTransactionManager createTransactionManager(final LocalContainerEntityManagerFactoryBean emf) {
        final JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(emf.getObject());
        return txManager;
    }

}
