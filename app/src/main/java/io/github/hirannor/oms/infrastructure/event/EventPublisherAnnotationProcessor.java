package io.github.hirannor.oms.infrastructure.event;

import io.github.hirannor.oms.infrastructure.aggregate.Evented;
import io.github.hirannor.oms.infrastructure.messaging.MessagePublisher;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * An annotation processor for processing events, such as publishing and clearing them from domain
 *
 * @author Mate Karolyi
 */
@Aspect
@Component
class EventPublisherAnnotationProcessor {
    private final MessagePublisher messages;

    EventPublisherAnnotationProcessor(final MessagePublisher messages) {
        this.messages = messages;
    }

    @After("@annotation(io.github.hirannor.oms.infrastructure.event.EventPublisher)")
    void afterMethodExecution(final JoinPoint point) {
        final Object[] methodArguments = point.getArgs();

        for (final Object singleMethodArgument : methodArguments) {
            if (singleMethodArgument instanceof final Object[] varArgs) {
                for (final Object singleArg : varArgs) {
                    publishAndClearEventsIfEvented(singleArg);
                }
            }
            if (singleMethodArgument instanceof final Collection<?> elements) {
                for (final Object singleElement : elements) {
                    publishAndClearEventsIfEvented(singleElement);
                }
            }
            publishAndClearEventsIfEvented(singleMethodArgument);
        }
    }

    private void publishAndClearEventsIfEvented(final Object param) {
        if (param instanceof final Evented evt) {
            messages.publish(evt.events());
            evt.clearEvents();
        }
    }
}
