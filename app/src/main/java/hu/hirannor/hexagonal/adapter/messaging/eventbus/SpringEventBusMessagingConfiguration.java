package hu.hirannor.hexagonal.adapter.messaging.eventbus;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class for event bus messaging adapter.
 *
 * @author Mate Karolyi
 */
@Configuration
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.messaging",
        havingValue = "spring-event-bus",
        matchIfMissing = true
)
public class SpringEventBusMessagingConfiguration {

    SpringEventBusMessagingConfiguration() {
    }

}
