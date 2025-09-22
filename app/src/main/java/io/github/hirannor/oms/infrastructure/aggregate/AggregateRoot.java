package io.github.hirannor.oms.infrastructure.aggregate;

import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.event.Evented;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
