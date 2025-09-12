package hu.hirannor.hexagonal.adapter.web.rest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Spring configuration class for REST API adapter.
 *
 * @author Mate Karolyi
 */
@Configuration
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.web",
        havingValue = "rest",
        matchIfMissing = true
)
@EnableWebSecurity
public class RestApiConfiguration {


    private final AuthenticationFilter authFilter;

    public RestApiConfiguration(AuthenticationFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/customers", "/api/customers/**").authenticated()
                    .anyRequest().permitAll()
            )
            .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
