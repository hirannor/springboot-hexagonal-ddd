package io.github.hirannor.oms.adapter.payment.mock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.payment",
        havingValue = "mock"
)
public class MockPaymentConfiguration {
}
