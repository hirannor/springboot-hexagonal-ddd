package hu.hirannor.hexagonal.adapter.authentication.jwt;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@ComponentScan
@ConditionalOnProperty(
    value = "adapter.authentication",
    havingValue = "jwt"
)
public class JwtAuthenticationConfiguration {

    @Bean
    BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
