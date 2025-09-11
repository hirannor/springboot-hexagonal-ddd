package hu.hirannor.hexagonal.adapter.authentication.basic;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Spring configuration class for basic authentication adapter.
 *
 * @author Mate Karolyi
 */
@Configuration
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.authentication",
        havingValue = "basic",
        matchIfMissing = true
)
public class BasicAuthenticationConfiguration {

    BasicAuthenticationConfiguration() {
    }

    @Bean
    SecurityFilterChain createSecurityFilterChain(final HttpSecurity http)
            throws Exception {
        http.authorizeHttpRequests(authz ->
                    authz
                        .requestMatchers("/error", "/h2-console/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .httpBasic(withDefaults())
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    UserDetailsService createUserDetailsService() {
        final UserDetails user = User
                .withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

}
