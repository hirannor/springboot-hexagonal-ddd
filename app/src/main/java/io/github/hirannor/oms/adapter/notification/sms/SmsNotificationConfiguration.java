package io.github.hirannor.oms.adapter.notification.sms;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.notification",
        havingValue = "sms"
)
public class SmsNotificationConfiguration {
}
