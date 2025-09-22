package io.github.hirannor.oms.adapter.authentication.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.SecretKey;
import java.util.Base64;

@Configuration
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.authentication",
        havingValue = "jwt"
)
@EnableConfigurationProperties(JwtAuthenticationConfigurationProperties.class)
public class JwtAuthenticationConfiguration {

    private final JwtAuthenticationConfigurationProperties properties;

    @Autowired
    public JwtAuthenticationConfiguration(final JwtAuthenticationConfigurationProperties properties) {
        this.properties = properties;
    }

    @Bean
    BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecretKey createJwtSecretKey() {
        return switch (properties.getAlgorithm()) {
            case HS256, HS384, HS512 -> Keys.hmacShaKeyFor(Base64.getDecoder().decode(properties.getSecret()));
        };
    }
}
