package io.github.hirannor.oms.adapter.persistence.inmemory;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * In memory persistence related configuration
 *
 * @author Mate Karolyi
 */
@Configuration
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.persistence",
        havingValue = "in-memory"
)
public class InMemoryPersistenceConfiguration {

    InMemoryPersistenceConfiguration() {
    }
}
