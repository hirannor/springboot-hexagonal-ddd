package io.github.hirannor.oms.adapter.notification.email;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "email")
public class EmailNotificationConfigurationProperties {
    private String from;

    public EmailNotificationConfigurationProperties() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(final String from) {
        this.from = from;
    }
}
