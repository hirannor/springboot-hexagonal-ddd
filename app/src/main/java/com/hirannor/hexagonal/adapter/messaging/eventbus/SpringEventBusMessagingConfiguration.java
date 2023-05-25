package com.hirannor.hexagonal.adapter.messaging.eventbus;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Spring configuration class for event bus messaging adapter.
 *
 * @author Mate Karolyi
 */
@EnableAsync
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
