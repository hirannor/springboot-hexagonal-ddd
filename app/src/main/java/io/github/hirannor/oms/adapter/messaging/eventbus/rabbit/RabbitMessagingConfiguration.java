package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan
@ConditionalOnProperty(
        value = "adapter.messaging",
        havingValue = "rabbitmq"
)
@EnableScheduling
@EnableConfigurationProperties(RabbitConfigurationProperties.class)
public class RabbitMessagingConfiguration {

    private static final String X_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";
    private static final String X_DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";

    private final RabbitConfigurationProperties properties;
    private final ConnectionFactory connectionFactory;
    private final ObjectMapper objectMapper;

    @Autowired
    RabbitMessagingConfiguration(final RabbitConfigurationProperties properties,
                                 final ConnectionFactory connectionFactory,
                                 final ObjectMapper objectMapper) {
        this.properties = properties;
        this.connectionFactory = connectionFactory;
        this.objectMapper = objectMapper;
    }

    @Bean
    TopicExchange createOmsExchange() {
        return new TopicExchange(properties.getExchange());
    }

    @Bean
    @Qualifier("omsQueue")
    Queue createOmsQueue() {
        return QueueBuilder.durable(properties.getQueue())
                .withArgument(X_DEAD_LETTER_EXCHANGE, properties.getExchange())
                .withArgument(X_DEAD_LETTER_ROUTING_KEY, properties.getDlq())
                .build();
    }


    @Bean
    @Qualifier("omsDeadLetterQueue")
    Queue createOmsDeadLetterQueue() {
        return QueueBuilder.durable(properties.getDlq()).build();
    }

    @Bean
    Binding createOmsDlqBinding(@Qualifier("omsDeadLetterQueue") final Queue omsDeadLetterQueue, final TopicExchange omsExchange) {
        return BindingBuilder.bind(omsDeadLetterQueue).to(omsExchange).with(properties.getDlq());
    }

    @Bean
    public Binding createOmsQueueBinding(@Qualifier("omsQueue") final Queue omsQueue, final TopicExchange omsExchange) {
        return BindingBuilder.bind(omsQueue).to(omsExchange).with("#");
    }

    @Bean
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(createJacksonMessageConverter());
        factory.setAdviceChain(
                RetryInterceptorBuilder.stateless()
                        .maxAttempts(properties.getRetry().getMaxAttempts())
                        .backOffOptions(1000, 2.0, 10000)
                        .recoverer(new RejectAndDontRequeueRecoverer())
                        .build()
        );
        return factory;
    }


    @Bean
    RabbitTemplate rabbitTemplate() {
        final RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(createJacksonMessageConverter());

        return template;
    }

    private Jackson2JsonMessageConverter createJacksonMessageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

}
