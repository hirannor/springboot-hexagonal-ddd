package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Objects;

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
    private final ObjectMapper objectMapper;
    private final RabbitProperties rabbitProperties;
    private final RabbitMessageConfirmCallbackHandler callbackHandler;

    @Autowired
    RabbitMessagingConfiguration(final RabbitConfigurationProperties properties,
                                 final ObjectMapper objectMapper,
                                 final RabbitProperties rabbitProperties,
                                 final RabbitMessageConfirmCallbackHandler callbackHandler) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.rabbitProperties = rabbitProperties;
        this.callbackHandler = callbackHandler;
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
    Binding createOmsQueueBinding(@Qualifier("omsQueue") final Queue omsQueue, final TopicExchange omsExchange) {
        return BindingBuilder.bind(omsQueue).to(omsExchange).with("#");
    }


    @Bean
    ConnectionFactory connectionFactory() {
        final CachingConnectionFactory connection = new CachingConnectionFactory();
        connection.setHost(rabbitProperties.getHost());
        connection.setPort(rabbitProperties.getPort());
        connection.setUsername(rabbitProperties.getUsername());
        connection.setPassword(rabbitProperties.getPassword());
        connection.setPublisherReturns(rabbitProperties.isPublisherReturns());
        connection.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        connection.setPublisherReturns(true);
        return connection;
    }

    @Bean
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(final SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, this.connectionFactory());
        Objects.requireNonNull(factory);

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
        final RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter(createJacksonMessageConverter());
        template.setConfirmCallback(callbackHandler);
        template.setReturnsCallback(callbackHandler);

        return template;
    }

    private Jackson2JsonMessageConverter createJacksonMessageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

}
