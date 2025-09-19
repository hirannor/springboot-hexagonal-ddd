package hu.hirannor.hexagonal.infrastructure.aggregate;

import hu.hirannor.hexagonal.infrastructure.event.DomainEvent;
import java.util.*;

/**
 * Abstraction of aggregate root
 *
 * @author Mate Karolyi
 */
public abstract class AggregateRoot implements Evented {

    private final List<DomainEvent> events;

    public AggregateRoot() {
        events = new ArrayList<>();
    }

    @Override
    public List<DomainEvent> events() {
        return Collections.unmodifiableList(events);
    }

    @Override
    public void clearEvents() {
        events.clear();
    }

}
