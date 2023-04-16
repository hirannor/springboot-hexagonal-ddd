package com.hirannor.hexagonal.application.usecase.eventing;

import com.hirannor.hexagonal.infrastructure.event.Event;

import java.util.List;

public interface EventPublishing {

    void publish(List<? extends Event> events);

}
