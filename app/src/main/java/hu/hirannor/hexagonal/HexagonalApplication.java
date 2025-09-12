package hu.hirannor.hexagonal;

import hu.hirannor.hexagonal.adapter.authentication.jwt.JwtAuthenticationConfiguration;
import hu.hirannor.hexagonal.adapter.messaging.eventbus.SpringEventBusMessagingConfiguration;
import hu.hirannor.hexagonal.adapter.notification.email.EmailNotificationConfiguration;
import hu.hirannor.hexagonal.adapter.notification.sms.SmsNotificationConfiguration;
import hu.hirannor.hexagonal.adapter.persistence.inmemory.InMemoryPersistenceConfiguration;
import hu.hirannor.hexagonal.adapter.persistence.jpa.JpaPersistenceConfiguration;
import hu.hirannor.hexagonal.adapter.web.rest.RestApiConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;

/**
 * Main entry point of application.
 *
 * @author Mate Karolyi
 */
@Import({
        JpaPersistenceConfiguration.class,
        InMemoryPersistenceConfiguration.class,
        RestApiConfiguration.class,
        JwtAuthenticationConfiguration.class,
        SpringEventBusMessagingConfiguration.class,
        EmailNotificationConfiguration.class,
        SmsNotificationConfiguration.class
})
@SpringBootApplication(
        exclude = {
            org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration.class,
            org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
            org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
            org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
        }
)
@ComponentScan(
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.REGEX,
                        pattern = "hu.hirannor.hexagonal.adapter.*"
                ),
        }
)
public class HexagonalApplication {

    public static void main(String[] args) {
        SpringApplication.run(HexagonalApplication.class, args);
    }

}
