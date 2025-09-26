package io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.mapping;

import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageModel;
import io.github.hirannor.oms.adapter.persistence.jpa.outbox.message.MessageModelMapper;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component(value = "DomainEventModelToEventJpaMapper")
public class MessageModelToEventMapper implements Function<MessageModel, Message> {

    private final Map<Class<? extends MessageModel>, MessageModelMapper<?, ?>> registry;

    @Autowired
    public MessageModelToEventMapper(final List<MessageModelMapper<?, ?>> mappers) {
        this.registry = mappers.stream()
                .collect(Collectors.toMap(MessageModelMapper::messageType, m -> m));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Message apply(final MessageModel model) {
        if (model == null) return null;

        final MessageModelMapper<MessageModel, DomainEvent> mapper = (MessageModelMapper<MessageModel, DomainEvent>) registry.get(model.getClass());
        if (mapper == null) return null;

        return mapper.apply(model);
    }
}
