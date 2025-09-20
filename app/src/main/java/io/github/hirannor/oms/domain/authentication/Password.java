package io.github.hirannor.oms.domain.authentication;

import java.util.Objects;
import java.util.regex.Pattern;

public record Password(String value) {

    private static final int MIN_LENGTH = 8;
    private static final Pattern STRONG_PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$");

    public Password {
        Objects.requireNonNull(value, "Password cannot be null");
        if (value.length() < MIN_LENGTH)
            throw new IllegalArgumentException("Password must be at least " + MIN_LENGTH + " characters long");
        if (!STRONG_PASSWORD_PATTERN.matcher(value).matches())
            throw new IllegalArgumentException("Password must contain at least one uppercase letter, one lowercase letter, and one digit");
    }

    public static Password from(final String password) {
        return new Password(password);
    }

}