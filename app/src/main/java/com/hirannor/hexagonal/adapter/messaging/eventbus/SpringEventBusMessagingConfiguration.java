package com.hirannor.hexagonal.adapter.messaging.eventbus;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Configuration
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.messaging",
        havingValue = "spring-event-bus",
        matchIfMissing = true
)
public class SpringEventBusMessagingConfiguration {

    SpringEventBusMessagingConfiguration() {}

}
