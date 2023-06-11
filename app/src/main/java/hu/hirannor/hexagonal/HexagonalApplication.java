package hu.hirannor.hexagonal;

import hu.hirannor.hexagonal.adapter.authentication.basic.BasicAuthenticationConfiguration;
import hu.hirannor.hexagonal.adapter.messaging.eventbus.SpringEventBusMessagingConfiguration;
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
        RestApiConfiguration.class,
        BasicAuthenticationConfiguration.class,
        SpringEventBusMessagingConfiguration.class
})
@SpringBootApplication
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
