package com.hirannor.hexagonal.domain.customer;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record EmailAddress(String value) {

    private static final String EMAIL_REGEX_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\" +
            ".[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";

    public EmailAddress {
        if (value == null || value.isEmpty()) throw new IllegalArgumentException("EmailAddress cannot be null!");

        if (!isValidEmailAddress(value)) throw new IllegalArgumentException("Format of email-address is not valid!");
    }

    public static EmailAddress from(final String value) {
        return new EmailAddress(value);
    }

    private boolean isValidEmailAddress(final String value) {
        final Pattern p = Pattern.compile(EMAIL_REGEX_PATTERN);
        final Matcher m = p.matcher(value);

        return m.matches();

    }
}
