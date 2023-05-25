package com.hirannor.hexagonal.domain.customer;

import java.util.regex.Pattern;

public record EmailAddress(String value) {

    private static final String EMAIL_REGEX =
        "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\" +
            ".[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public EmailAddress {
        if (value == null || value.isEmpty()) throw new IllegalArgumentException("EmailAddress cannot be null!");

        if (!isValidEmailAddress(value)) throw new IllegalArgumentException("Format of email-address is not valid!");
    }

    public static EmailAddress from(final String value) {
        return new EmailAddress(value);
    }

    private boolean isValidEmailAddress(final String value) {
        return EMAIL_PATTERN.matcher(value).matches();
    }
}
