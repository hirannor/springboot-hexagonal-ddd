package hu.hirannor.hexagonal.adapter.authentication.jwt;

import hu.hirannor.hexagonal.application.port.Authenticator;
import hu.hirannor.hexagonal.domain.EmailAddress;
import hu.hirannor.hexagonal.domain.authentication.*;
import hu.hirannor.hexagonal.domain.error.CustomerNotFound;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.function.Supplier;

/**
 * Naive implementation of {@link Authenticator}
 *
 * @author Mate Karolyi
 */
@Component
class JwtAuthentication implements Authenticator {

    private static final Logger LOGGER = LogManager.getLogger(
        JwtAuthentication.class
    );

    private final AuthenticationRepository authentications;
    private final Key jwtKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final BCryptPasswordEncoder encoder;

    @Autowired
    JwtAuthentication(final AuthenticationRepository authentications,
                      final BCryptPasswordEncoder encoder) {
        this.authentications = authentications;
        this.encoder = encoder;
    }

    @Override
    public AuthenticationResult authenticate(final AuthUser user) {
        if (user == null) throw new IllegalArgumentException("auth cannot be null");

        final AuthUser storedUser = authentications.findByEmail(user.emailAddress())
                .orElseThrow(failBecauseEmailAddressWasNotFound(user.emailAddress()));

        if (!encoder.matches(user.password().value(), storedUser.password().value()))
            throw new IllegalStateException("Invalid password");

        final String token = generateToken(storedUser.emailAddress());

        return AuthenticationResult.from(
                user.emailAddress(),
                token
        );
    }

    @Override
    public AuthUser validateToken(final String token) {
        if (token == null) throw new IllegalArgumentException("token is null");

        try {
            final String email = Jwts.parserBuilder()
                    .setSigningKey(jwtKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return AuthUser.of(EmailAddress.from(email), null);
        } catch(final SignatureException ex) {
            throw new AuthenticationServiceException("Invalid JWT signature", ex);
        }
    }

    @Override
    public void register(final AuthUser auth) {
        if (auth == null) throw new IllegalArgumentException("auth cannot be null");

        final AuthUser hashedUser = new AuthUser(
                auth.emailAddress(),
                new Password(encoder.encode(auth.password().value()))
        );

        authentications.save(hashedUser);
    }

    private Supplier<CustomerNotFound> failBecauseEmailAddressWasNotFound(final EmailAddress emailAddress) {
        return () -> new CustomerNotFound("Customer was not found by: " + emailAddress.value());
    }

    private String generateToken(final EmailAddress email) {
        final Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(email.value())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(3600)))
                .signWith(jwtKey)
                .compact();
    }
}
