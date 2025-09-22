package io.github.hirannor.oms.adapter.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hirannor.oms.adapter.web.rest.error.ProblemDetailsModel;
import io.github.hirannor.oms.application.port.authentication.Authenticator;
import io.github.hirannor.oms.application.service.authentication.error.InvalidJwtToken;
import io.github.hirannor.oms.domain.authentication.AuthUser;
import io.github.hirannor.oms.domain.authentication.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.function.Function;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LogManager.getLogger(
            JwtAuthenticationFilter.class
    );

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private static final String[] WHITELISTED_PATHS = {
            "/swagger-ui",
            "/swagger-ui.html",
            "/swagger-ui/index.html",
            "/v3/api-docs",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/webjars/**",
            "/api/register",
            "/api/auth",
            "/api/auth/refresh",
            "/api/payments/stripe/webhook"
    };

    private static final String BEARER = "Bearer ";

    private final Function<Role, PermissionRoleModel> mapRoleToModel;

    private final Authenticator authenticator;
    private final ObjectMapper mapper;

    @Autowired
    JwtAuthenticationFilter(final Authenticator authenticator,
                            final ObjectMapper mapper) {
        this(authenticator, mapper, new RoleToPermissionRoleModelMapper());
    }

    JwtAuthenticationFilter(final Authenticator authenticator,
                            final ObjectMapper mapper,
                            final Function<Role, PermissionRoleModel> mapRoleToModel) {
        this.authenticator = authenticator;
        this.mapper = mapper;
        this.mapRoleToModel = mapRoleToModel;
    }

    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) {
        final String path = request.getRequestURI();
        return Arrays.stream(WHITELISTED_PATHS)
                .anyMatch(pattern -> PATH_MATCHER.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            sendError(request, response, "Missing or invalid Authorization header");
            return;
        }

        final String token = authHeader.substring(BEARER.length()).trim();

        try {
            final AuthUser auth = authenticator.validateAccessToken(token);

            final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    auth.emailAddress().value(),
                    null,
                    auth.roles()
                            .stream()
                            .map(mapRoleToModel
                                    .andThen(JwtAuthenticationFilter::addRolePrefix))
                            .toList()
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request, response);
        } catch (final InvalidJwtToken ex) {
            sendError(request, response, ex.getMessage());
        }
    }

    private void sendError(final HttpServletRequest request,
                           final HttpServletResponse response,
                           final String detail) throws IOException {

        LOGGER.warn("Unauthorized access to {}: {}", request.getRequestURI(), detail);

        final ProblemDetailsModel problem = ProblemDetailsModel.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .title(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .detail(detail)
                .instance(request.getRequestURI())
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        mapper.writeValue(response.getWriter(), problem);
    }

    private static SimpleGrantedAuthority addRolePrefix(final PermissionRoleModel role) {
        return new SimpleGrantedAuthority("ROLE_" + role.value());
    }
}
