package io.github.hirannor.oms.adapter.web.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@EnableWebSecurity
@Configuration
class SecurityConfiguration {
    private final JwtAuthenticationFilter authFilter;
    private final AccessDeniedHandler accessDenied;

    SecurityConfiguration(final JwtAuthenticationFilter authFilter,
                          final AccessDeniedHandler accessDenied) {
        this.authFilter = authFilter;
        this.accessDenied = accessDenied;
    }

    @Bean
    SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Customers API
                        .requestMatchers(HttpMethod.GET, "/api/customers")
                        .hasRole(PermissionRoleModel.ADMIN.value())
                        .requestMatchers(HttpMethod.DELETE, "/api/customers/*")
                        .hasRole(PermissionRoleModel.ADMIN.value())

                        .requestMatchers(HttpMethod.GET, "/api/customers/*")
                        .hasAnyRole(PermissionRoleModel.CUSTOMER.value(), PermissionRoleModel.ADMIN.value())

                        .requestMatchers(HttpMethod.PUT, "/api/customers/*")
                        .hasRole(PermissionRoleModel.CUSTOMER.value())

                        // Products API
                        .requestMatchers(HttpMethod.GET, "/api/products")
                        .hasAnyRole(PermissionRoleModel.ADMIN.value(), PermissionRoleModel.CUSTOMER.value())
                        .requestMatchers(HttpMethod.GET, "/api/products/*")
                        .hasAnyRole(PermissionRoleModel.ADMIN.value(), PermissionRoleModel.CUSTOMER.value())

                        .requestMatchers(HttpMethod.POST, "/api/products")
                        .hasRole(PermissionRoleModel.ADMIN.value())
                        .requestMatchers(HttpMethod.DELETE, "/api/products/*")
                        .hasRole(PermissionRoleModel.ADMIN.value())

                        // Baskets API
                        .requestMatchers(HttpMethod.GET, "/api/baskets")
                        .hasRole(PermissionRoleModel.ADMIN.value())
                        .requestMatchers(HttpMethod.DELETE, "/api/baskets/*")
                        .hasRole(PermissionRoleModel.ADMIN.value())

                        .requestMatchers(HttpMethod.GET, "/api/baskets/*")
                        .hasRole(PermissionRoleModel.CUSTOMER.value())
                        .requestMatchers(HttpMethod.POST, "/api/baskets")
                        .hasRole(PermissionRoleModel.CUSTOMER.value())

                        // Orders API
                        .requestMatchers(HttpMethod.PATCH, "/api/orders/*/status")
                        .hasRole(PermissionRoleModel.ADMIN.value())

                        .requestMatchers(HttpMethod.POST, "/api/orders/*/cancel", "/api/orders/*/pay")
                        .hasRole(PermissionRoleModel.CUSTOMER.value())
                        .requestMatchers(HttpMethod.GET, "/api/orders/*")
                        .hasRole(PermissionRoleModel.CUSTOMER.value())
                        .requestMatchers(HttpMethod.POST, "/api/orders")
                        .hasRole(PermissionRoleModel.CUSTOMER.value())

                        // Everything else
                        .anyRequest().permitAll()
                )
                .addFilterBefore(authFilter, BasicAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDenied)
                );

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
