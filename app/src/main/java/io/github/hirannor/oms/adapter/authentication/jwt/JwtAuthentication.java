package io.github.hirannor.oms.adapter.authentication.jwt;

import io.github.hirannor.oms.application.port.authentication.Authenticator;
import io.github.hirannor.oms.application.service.authentication.error.AuthUserNotFound;
import io.github.hirannor.oms.application.service.authentication.error.InvalidJwtToken;
import io.github.hirannor.oms.application.service.authentication.error.InvalidPassword;
import io.github.hirannor.oms.domain.authentication.*;
import io.github.hirannor.oms.domain.core.valueobject.EmailAddress;
import io.github.hirannor.oms.infrastructure.adapter.DriverAdapter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
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

    private static final String OMS_REFRESH_AUDIENCE = "oms-refresh";

    private final Function<RoleModel, Role> mapRoleModelToRole;

    private final AuthenticationRepository authentications;
    private final SecretKey key;
    private final BCryptPasswordEncoder encoder;
    private final JwtAuthenticationConfigurationProperties properties;

    @Autowired
    JwtAuthentication(final AuthenticationRepository authentications,
                      final BCryptPasswordEncoder encoder,
                      final SecretKey key,
                      final JwtAuthenticationConfigurationProperties properties) {
        this(authentications, encoder, new RoleModelToRoleMapper(), key, properties);
    }

    JwtAuthentication(final AuthenticationRepository authentications,
                      final BCryptPasswordEncoder encoder,
                      final Function<RoleModel, Role> mapRoleModelToRole,
                      final SecretKey key,
                      final JwtAuthenticationConfigurationProperties properties) {
        this.authentications = authentications;
        this.encoder = encoder;
        this.mapRoleModelToRole = mapRoleModelToRole;
        this.key = key;
        this.properties = properties;
    }

    @Override
    public AuthenticationResult authenticate(final AuthUser user) {
        if (user == null) throw new IllegalArgumentException("auth cannot be null");

        final AuthUser storedUser = authentications.findByEmail(user.emailAddress())
                .orElseThrow(failBecauseEmailAddressWasNotFound(user.emailAddress()));

        final String providedPassword = user.password().value();
        final String storedPassword = storedUser.password().value();

        validatePassword(providedPassword, storedPassword);

        final String accessToken = generateAccessToken(storedUser);
        final String refreshToken = generateRefreshToken(storedUser);

        return AuthenticationResult.from(
                user.emailAddress(),
                accessToken,
                refreshToken
        );
    }

    @Override
    public AuthenticationResult refresh(final RefreshToken cmd) {
        try {
            final Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(cmd.refreshToken())
                    .getPayload();

            if (!claims.getAudience().contains(OMS_REFRESH_AUDIENCE))
                throw new InvalidJwtToken("Not a refresh token");

            final String email = claims.getSubject();
            final Set<Role> roles = ((List<String>) claims.get("roles"))
                    .stream()
                    .map(mapToRoleModel().andThen(mapRoleModelToRole))
                    .collect(Collectors.toSet());

            final AuthUser user = AuthUser.of(EmailAddress.from(email), null, roles);

            return AuthenticationResult.from(
                    user.emailAddress(),
                    generateAccessToken(user),
                    generateRefreshToken(user)
            );
        } catch (ExpiredJwtException ex) {
            throw new InvalidJwtToken("Refresh token expired");
        } catch (JwtException ex) {
            throw new InvalidJwtToken("Invalid refresh token");
        }
    }

    @Override
    public AuthUser validateAccessToken(final String token) {
        try {
            final Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            if (!claims.getAudience().contains(properties.getAudience()))
                throw new InvalidJwtToken("Not an access token");

            final String email = claims.getSubject();
            final Set<Role> roles = ((List<String>) claims.get("roles"))
                    .stream()
                    .map(mapToRoleModel()
                            .andThen(mapRoleModelToRole))
                    .collect(Collectors.toSet());

            return AuthUser.of(EmailAddress.from(email), null, roles);
        } catch (final ExpiredJwtException ex) {
            throw new InvalidJwtToken("Token expired");
        } catch (final JwtException ex) {
            throw new InvalidJwtToken("Invalid JWT accessToken");
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

    private String generateAccessToken(final AuthUser user) {
        final Instant now = Instant.now();

        final List<String> roles = user.roles()
                .stream()
                .map(Enum::name)
                .toList();

        return Jwts.builder()
                .subject(user.emailAddress().value())
                .claim("roles", roles)
                .issuer(properties.getIssuer())
                .audience().add(properties.getAudience()).and()
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(properties.getAccessExpiration())))
                .signWith(key)
                .compact();
    }

    private String generateRefreshToken(final AuthUser user) {
        final Instant now = Instant.now();

        return Jwts.builder()
                .subject(user.emailAddress().value())
                .claim("roles", user.roles().stream().map(Enum::name).toList())
                .issuer(properties.getIssuer())
                .audience().add("oms-refresh").and()
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(properties.getRefreshExpiration())))
                .signWith(key)
                .compact();
    }

    private void validatePassword(final String providedPassword, final String storedPassword) {
        if (!encoder.matches(providedPassword, storedPassword))
            throw new InvalidPassword("Invalid password");
    }

    private Function<String, RoleModel> mapToRoleModel() {
        return RoleModel::from;
    }
}
