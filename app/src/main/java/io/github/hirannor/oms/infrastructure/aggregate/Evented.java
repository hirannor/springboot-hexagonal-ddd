package io.github.hirannor.oms.infrastructure.aggregate;

import io.github.hirannor.oms.infrastructure.event.DomainEvent;

import java.util.List;

/**
 * @author Mate Karolyi
 */
public interface Evented {
    /**
     * Retrieves a list of domain events
     *
     * @return a list of {@link DomainEvent}
     */
    List<DomainEvent> events();

    /**
     * clears domain events
     */
    void clearEvents();
}
