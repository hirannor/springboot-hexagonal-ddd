package io.github.hirannor.oms;

import io.github.hirannor.oms.adapter.authentication.jwt.JwtAuthenticationConfiguration;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.RabbitMessagingConfiguration;
import io.github.hirannor.oms.adapter.notification.email.EmailNotificationConfiguration;
import io.github.hirannor.oms.adapter.notification.sms.SmsNotificationConfiguration;
import io.github.hirannor.oms.adapter.payment.mock.MockPaymentConfiguration;
import io.github.hirannor.oms.adapter.payment.stripe.StripePaymentConfiguration;
import io.github.hirannor.oms.adapter.persistence.inmemory.InMemoryPersistenceConfiguration;
import io.github.hirannor.oms.adapter.persistence.jpa.JpaPersistenceConfiguration;
import io.github.hirannor.oms.adapter.web.rest.RestApiConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

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
        RabbitMessagingConfiguration.class,
        EmailNotificationConfiguration.class,
        SmsNotificationConfiguration.class,
        StripePaymentConfiguration.class,
        MockPaymentConfiguration.class
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
                        pattern = "io.github.hirannor.oms.adapter.*"
                ),
        }
)
public class OrderManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderManagementSystemApplication.class, args);
    }

}
