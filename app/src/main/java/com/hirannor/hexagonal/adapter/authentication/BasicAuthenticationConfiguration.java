package com.hirannor.hexagonal.adapter.authentication;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ComponentScan
@ConditionalOnProperty(value = "adapter.authentication", havingValue = "basic")
public class BasicAuthenticationConfiguration {

    BasicAuthenticationConfiguration() {
    }

    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity http)
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
    UserDetailsService userDetailsService() {
        final UserDetails user = User
                .withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

}
