package com.hirannor.hexagonal.application.port.messaging;

import com.hirannor.hexagonal.infrastructure.messaging.Message;

import java.util.List;

public interface MessagePublisher {

    void publish(List<? extends Message> messages);

}
