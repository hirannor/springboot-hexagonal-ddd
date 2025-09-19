package hu.hirannor.hexagonal.adapter.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.hirannor.hexagonal.application.port.authentication.Authenticator;
import hu.hirannor.hexagonal.domain.authentication.AuthUser;
import hu.hirannor.hexagonal.domain.authentication.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
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
            "/api/payments/stripe/webhook"
    };

    private static final String BEARER = "Bearer ";

    private final Function<Role, PermissionRoleModel> mapRoleToModel;

    private final Authenticator authenticator;
    private final ObjectMapper mapper;

    @Autowired
    AuthenticationFilter(final Authenticator authenticator,
                         final ObjectMapper mapper) {
      this(authenticator, mapper, new RoleToPermissionRoleModelMapper());
    }

    AuthenticationFilter(final Authenticator authenticator,
                         final ObjectMapper mapper,
                         final Function<Role, PermissionRoleModel> mapRoleToModel) {
        this.authenticator = authenticator;
        this.mapper = mapper;
        this.mapRoleToModel = mapRoleToModel;
    }

    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) throws ServletException {
        final String path = request.getRequestURI();
        return Arrays.stream(WHITELISTED_PATHS)
                .anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || authHeader.isBlank()) {
            sendError(request, response, "Authorization header is missing");
            return;
        }

        try {
            if (authHeader.startsWith(BEARER)) {
                final String token = authHeader.substring(BEARER.length()).trim();
                final AuthUser auth = authenticator.validateToken(token);

                final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        auth.emailAddress().value(),
                        null,
                        auth.roles()
                            .stream()
                            .map(mapRoleToModel
                                    .andThen(AuthenticationFilter::addRolePrefix))
                            .toList()
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                sendError(request, response, "Unsupported authentication notificationType");
                return;
            }
        } catch (Exception ex) {
            sendError(request, response, ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private void sendError(final HttpServletRequest request,
                           final HttpServletResponse response,
                           final String detail) throws IOException {

        final ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problem.setTitle("Forbidden");
        problem.setDetail(detail);
        problem.setInstance(URI.create(request.getRequestURI()));

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        mapper.writeValue(response.getWriter(), problem);
    }

    private static SimpleGrantedAuthority addRolePrefix(final PermissionRoleModel role) {
        return new SimpleGrantedAuthority("ROLE_" + role.value());
    }
}
