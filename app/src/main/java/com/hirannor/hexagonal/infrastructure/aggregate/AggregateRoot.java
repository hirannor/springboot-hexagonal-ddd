package com.hirannor.hexagonal.infrastructure.aggregate;

import com.hirannor.hexagonal.infrastructure.event.DomainEvent;

import java.util.List;

public interface AggregateRoot {

    void clearEvents();

    List<DomainEvent> listEvents();

}
