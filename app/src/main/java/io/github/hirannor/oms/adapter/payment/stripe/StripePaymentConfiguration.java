package io.github.hirannor.oms.adapter.payment.stripe;

import com.stripe.StripeClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.payment",
        havingValue = "stripe"
)
@EnableConfigurationProperties(StripeConfigurationProperties.class)
public class StripePaymentConfiguration {

    private final StripeConfigurationProperties config;

    public StripePaymentConfiguration(final StripeConfigurationProperties config) {
        this.config = config;
    }

    @Bean
    public StripeClient createStripeClient() {
        return new StripeClient(config.getApiKey());
    }

}
