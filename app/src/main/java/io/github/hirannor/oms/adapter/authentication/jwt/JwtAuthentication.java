package io.github.hirannor.oms.adapter.authentication.jwt;

import io.github.hirannor.oms.application.port.authentication.Authenticator;
import io.github.hirannor.oms.application.service.authentication.error.AuthUserNotFound;
import io.github.hirannor.oms.application.service.authentication.error.InvalidJwtToken;
import io.github.hirannor.oms.application.service.authentication.error.InvalidPassword;
import io.github.hirannor.oms.domain.authentication.*;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.infrastructure.adapter.DriverAdapter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Naive implementation of {@link Authenticator}
 *
 * @author Mate Karolyi
 */
@Component
@DriverAdapter
class JwtAuthentication implements Authenticator {

    private static final Logger LOGGER = LogManager.getLogger(
        JwtAuthentication.class
    );

    private final Function<RoleModel, Role> mapRoleModelToRole;

    private final AuthenticationRepository authentications;
    private final Key jwtKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final BCryptPasswordEncoder encoder;

    @Autowired
    JwtAuthentication(final AuthenticationRepository authentications,
                      final BCryptPasswordEncoder encoder) {
       this(authentications, encoder, new RoleModelToRoleMapper());
    }

    JwtAuthentication(final AuthenticationRepository authentications,
                      final BCryptPasswordEncoder encoder,
                      final Function<RoleModel, Role> mapRoleModelToRole) {
        this.authentications = authentications;
        this.encoder = encoder;
        this.mapRoleModelToRole = mapRoleModelToRole;
    }

    @Override
    public AuthenticationResult authenticate(final AuthUser user) {
        if (user == null) throw new IllegalArgumentException("auth cannot be null");

        final AuthUser storedUser = authentications.findByEmail(user.emailAddress())
                .orElseThrow(failBecauseEmailAddressWasNotFound(user.emailAddress()));

        if (!encoder.matches(user.password().value(), storedUser.password().value()))
            throw new InvalidPassword("Invalid password");

        final String token = generateTokenFrom(storedUser);

        return AuthenticationResult.from(
                user.emailAddress(),
                token
        );
    }

    @Override
    public AuthUser validateToken(final String token) {
        try {
            final Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            final String email = claims.getSubject();
            final Set<Role> roles = ((List<String>) claims.get("roles"))
                    .stream()
                    .map(mapToRoleModel()
                            .andThen(mapRoleModelToRole))
                    .collect(Collectors.toSet());

            return AuthUser.of(EmailAddress.from(email), null, roles);
        } catch (final Exception ex) {
            throw new InvalidJwtToken("Invalid JWT token");
        }
    }

    @Override
    public void register(final AuthUser auth) {
        if (auth == null) throw new IllegalArgumentException("auth cannot be null");

        final AuthUser hashedUser = new AuthUser(
                auth.emailAddress(),
                new Password(encoder.encode(auth.password().value())),
                auth.roles()
        );

        authentications.save(hashedUser);
    }

    private Supplier<AuthUserNotFound> failBecauseEmailAddressWasNotFound(final EmailAddress emailAddress) {
        return () -> new AuthUserNotFound("User was not found by: " + emailAddress.value());
    }

    private String generateTokenFrom(final AuthUser user) {
        final Instant now = Instant.now();

        final List<String> roles = user.roles()
                .stream()
                .map(Enum::name)
                .toList();

        return Jwts.builder()
                .setSubject(user.emailAddress().value())
                .claim("roles", roles)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(3600)))
                .signWith(jwtKey)
                .compact();
    }

    private Function<String, RoleModel> mapToRoleModel() {
        return RoleModel::from;
    }
}
