package com.hirannor.hexagonal.adapter.authentication;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

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
        http
            .authorizeHttpRequests()
            .antMatchers("/error", "/h2-console/**")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic()
            .and()
            .csrf()
            .disable()
            .headers().frameOptions()
            .disable();

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
