package io.github.hirannor.oms.adapter.authentication.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "security.jwt")
public class JwtAuthenticationConfigurationProperties {

    private String secret;

    private JwtKeyAlgorithm algorithm;

    private Duration accessExpiration;

    private Duration refreshExpiration;

    private String issuer;

    private String audience;

    public String getSecret() {
        return secret;
    }

    public void setSecret(final String secret) {
        this.secret = secret;
    }

    public JwtKeyAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(final JwtKeyAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public Duration getAccessExpiration() {
        return accessExpiration;
    }

    public void setAccessExpiration(final Duration accessExpiration) {
        this.accessExpiration = accessExpiration;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(final String issuer) {
        this.issuer = issuer;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(final String audience) {
        this.audience = audience;
    }

    public Duration getRefreshExpiration() {
        return refreshExpiration;
    }

    public void setRefreshExpiration(final Duration refreshExpiration) {
        this.refreshExpiration = refreshExpiration;
    }
}
