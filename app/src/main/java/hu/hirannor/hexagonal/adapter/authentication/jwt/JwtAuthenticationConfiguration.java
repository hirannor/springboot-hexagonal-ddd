package hu.hirannor.hexagonal.adapter.authentication.jwt;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@ConditionalOnProperty(
    value = "adapter.authentication",
    havingValue = "jwt"
)
public class JwtAuthenticationConfiguration {
}
