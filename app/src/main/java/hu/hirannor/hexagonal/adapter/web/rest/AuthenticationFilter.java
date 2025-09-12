package hu.hirannor.hexagonal.adapter.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.hirannor.hexagonal.adapter.web.rest.customer.model.ProblemDetailsModel;
import hu.hirannor.hexagonal.application.port.Authenticator;
import hu.hirannor.hexagonal.domain.authentication.AuthUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    private static final String[] EXCLUDED_PATHS = {
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
        return Arrays.stream(EXCLUDED_PATHS)
                .anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);


        if (authHeader == null || authHeader.isBlank()) {
            final ProblemDetailsModel headerIsMissing = createUnauthorizedProblemDetailsFrom(request, "Authorization header is missing!");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,  mapper.writeValueAsString(headerIsMissing));
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
                final ProblemDetailsModel unsupportedAuthMethod = createUnauthorizedProblemDetailsFrom(request, "Unsupported authentication method!");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, mapper.writeValueAsString(unsupportedAuthMethod));
                return;
            }
        } catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private ProblemDetailsModel createUnauthorizedProblemDetailsFrom(final HttpServletRequest request, final String detail) {
       return new ProblemDetailsModel()
                .status(HttpStatus.UNAUTHORIZED.value())
                .instance(request.getRequestURI())
                .title("Unauthorized")
                .timestamp(Instant.now())
                .detail(detail);
    }
}
