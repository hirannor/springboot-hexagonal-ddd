package io.github.hirannor.oms.adapter.notification.email;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.notification",
        havingValue = "email"
)
@EnableConfigurationProperties({MailProperties.class, EmailNotificationConfigurationProperties.class})
public class EmailNotificationConfiguration {

    private final MailProperties mailProps;

    public EmailNotificationConfiguration(final MailProperties mailProps) {
        this.mailProps = mailProps;
    }

    @Bean
    public JavaMailSender createJavaMailSender() {
        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(mailProps.getHost());
        mailSender.setPort(mailProps.getPort());
        mailSender.setUsername(mailProps.getUsername());
        mailSender.setPassword(mailProps.getPassword());

        final Properties props = mailSender.getJavaMailProperties();
        props.putAll(mailProps.getProperties());

        return mailSender;
    }
}
