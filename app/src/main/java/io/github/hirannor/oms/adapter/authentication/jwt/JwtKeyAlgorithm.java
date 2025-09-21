package io.github.hirannor.oms.adapter.authentication.jwt;

import java.util.Objects;

public enum JwtKeyAlgorithm {
    HS256("HS256"),
    HS384("HS384"),
    HS512("HS512");

    private final String displayText;

    JwtKeyAlgorithm(final String displayText) {
        this.displayText = displayText;
    }

    public static JwtKeyAlgorithm from(final String text) {
        Objects.requireNonNull(text, "Algorithm must not be null");

        for (final JwtKeyAlgorithm alg : JwtKeyAlgorithm.values()) {
            if (alg.displayText.equalsIgnoreCase(text)) {
                return alg;
            }
        }

        throw new IllegalArgumentException(String.format("Unexpected algorithm '%s'", text));
    }

    public String displayText() {
        return this.displayText;
    }
}
