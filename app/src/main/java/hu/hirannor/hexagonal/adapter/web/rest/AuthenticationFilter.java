package hu.hirannor.hexagonal.adapter.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.hirannor.hexagonal.adapter.web.rest.customer.model.ProblemDetailsModel;
import hu.hirannor.hexagonal.application.port.Authenticator;
import hu.hirannor.hexagonal.domain.authentication.AuthUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.Header;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

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
            "/api/auth"
    };

    private static final String BEARER = "Bearer ";

    private final Authenticator authenticator;
    private final ObjectMapper mapper;

    @Autowired
    AuthenticationFilter(Authenticator authenticator, final ObjectMapper mapper) {
        this.authenticator = authenticator;
        this.mapper = mapper;
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
                        List.of(new SimpleGrantedAuthority("USER"))
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                sendError(request, response, "Unsupported authentication type");
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

        final ProblemDetailsModel details = new ProblemDetailsModel()
                .status(HttpStatus.UNAUTHORIZED.value())
                .instance(request.getRequestURI())
                .title("Unauthorized")
                .timestamp(Instant.now())
                .detail(detail);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        mapper.writeValue(response.getWriter(), details);
    }
}
