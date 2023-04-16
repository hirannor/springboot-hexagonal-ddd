package com.hirannor.hexagonal.adapter.api.rest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.api",
        havingValue = "rest",
        matchIfMissing = true
)
public class RestApiConfiguration {

    RestApiConfiguration() {}

}
