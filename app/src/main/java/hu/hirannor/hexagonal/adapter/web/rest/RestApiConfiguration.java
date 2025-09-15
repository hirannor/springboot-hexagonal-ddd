package hu.hirannor.hexagonal.adapter.web.rest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class for REST API adapter.
 */
@Configuration
@ComponentScan
@ConditionalOnProperty(
    value = "adapter.web",
    havingValue = "rest",
    matchIfMissing = true
)
public class RestApiConfiguration {

}
