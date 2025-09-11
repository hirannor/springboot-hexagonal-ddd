package hu.hirannor.hexagonal.infrastructure.messaging;

import java.util.List;

/**
 * Interface for publishing type of {@link Message} messages.
 *
 * @author Mate Karolyi
 */
public interface MessagePublisher {

    /**
     * Publish a list of messages.
     *
     * @param messages {@link List<? extends Message>} to be published
     */
    void publish(List<? extends Message> messages);

}
