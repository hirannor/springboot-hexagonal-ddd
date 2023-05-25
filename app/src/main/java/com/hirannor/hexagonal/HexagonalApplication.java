package com.hirannor.hexagonal;

import com.hirannor.hexagonal.adapter.api.rest.RestApiConfiguration;
import com.hirannor.hexagonal.adapter.authentication.BasicAuthenticationConfiguration;
import com.hirannor.hexagonal.adapter.messaging.eventbus.SpringEventBusMessagingConfiguration;
import com.hirannor.hexagonal.adapter.persistence.jpa.JpaPersistenceConfiguration;
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
            pattern = "com.hirannor.hexagonal.adapter.*"
        ),
    }
)
public class HexagonalApplication {

    public static void main(String[] args) {
        SpringApplication.run(HexagonalApplication.class, args);
    }

}
