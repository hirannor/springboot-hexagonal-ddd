package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
public class RabbitConfig {
    private static final String EXCHANGE = "oms.exchange";
    private static final String QUEUE = "oms.queue";
    private static final String DLQ = "oms.queue.dlq";

    @Bean
    TopicExchange createOmsExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    @Qualifier("omsQueue")
    Queue createOmsQueue() {
        return QueueBuilder.durable(QUEUE)
                .withArgument("x-dead-letter-exchange", EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ)
                .build();
    }


    @Bean
    @Qualifier("omsDeadLetterQueue")
    Queue createOmsDeadLetterQueue() {
        return QueueBuilder.durable(DLQ).build();
    }

    @Bean
    Binding createOmsDlqBinding(@Qualifier("omsDeadLetterQueue") final Queue omsDeadLetterQueue, final TopicExchange omsExchange) {
        return BindingBuilder.bind(omsDeadLetterQueue).to(omsExchange).with(DLQ);
    }

    @Bean
    public Binding createOmsQueueBinding(@Qualifier("omsQueue") final Queue omsQueue, final TopicExchange omsExchange) {
        return BindingBuilder.bind(omsQueue).to(omsExchange).with("#");
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            final ConnectionFactory connectionFactory) {
        final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);

        factory.setAdviceChain(
                RetryInterceptorBuilder.stateless()
                        .maxAttempts(3)
                        .backOffOptions(1000, 2.0, 10000)
                        .recoverer(new RejectAndDontRequeueRecoverer())
                        .build()
        );
        return factory;
    }
}
