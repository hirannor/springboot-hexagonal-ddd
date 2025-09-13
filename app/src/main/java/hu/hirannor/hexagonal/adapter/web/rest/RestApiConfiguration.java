package hu.hirannor.hexagonal.adapter.web.rest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring configuration class for REST API adapter.
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
                        // Customers API
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/customers")
                        .hasRole(PermissionRoleModel.ADMIN.value())
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/customers/**")
                        .hasRole(PermissionRoleModel.CUSTOMER.value())
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/customers/**")
                        .hasRole(PermissionRoleModel.CUSTOMER.value())
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/customers/**")
                        .hasRole(PermissionRoleModel.ADMIN.value())

                        // Products API
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/products/**")
                        .hasAnyRole(PermissionRoleModel.ADMIN.value(), PermissionRoleModel.CUSTOMER.value())
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/products")
                        .hasRole(PermissionRoleModel.ADMIN.value())
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/products/**")
                        .hasRole(PermissionRoleModel.ADMIN.value())

                        // Orders API
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/orders/**")
                        .hasRole(PermissionRoleModel.CUSTOMER.value())
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/orders")
                        .hasRole(PermissionRoleModel.CUSTOMER.value())
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/orders/**")
                        .hasRole(PermissionRoleModel.ADMIN.value())

                        .anyRequest().permitAll()
                )
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
