package io.github.hirannor.oms.adapter.notification.email;

import io.github.hirannor.oms.application.port.notification.EmailNotificationMessage;
import io.github.hirannor.oms.application.port.notification.NotificationMessage;
import io.github.hirannor.oms.application.port.notification.Notificator;
import io.github.hirannor.oms.infrastructure.adapter.DrivenAdapter;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@DrivenAdapter
class EmailNotificator implements Notificator {

    private static final Logger LOGGER = LogManager.getLogger(
            EmailNotificator.class
    );

    private final EmailNotificationConfigurationProperties config;
    private final JavaMailSender mailSender;
    private final TemplateEngine template;

    public EmailNotificator(final EmailNotificationConfigurationProperties config,
                            final JavaMailSender mailSender,
                            final TemplateEngine template) {
        this.config = config;
        this.mailSender = mailSender;
        this.template = template;
    }

    @Override
    public void send(final NotificationMessage notification) {
        if (!(notification instanceof EmailNotificationMessage message)) {
            throw new IllegalArgumentException("Invalid notification notificationType");
        }

        final Context context = new Context();
        context.setVariables(message.variables());
        final String html = template.process(message.templateName(), context);

        try {
            final MimeMessage mimeMessage = mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(config.getFrom());
            helper.setTo(message.recipient());
            helper.setSubject(message.subject());
            helper.setText(html, true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
