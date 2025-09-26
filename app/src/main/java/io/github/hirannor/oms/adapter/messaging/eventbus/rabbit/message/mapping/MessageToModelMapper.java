package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageMapper;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageModel;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class MessageToModelMapper implements Function<Message, MessageModel> {

    private final Map<Class<? extends Message>, MessageMapper<?, ?>> registry;

    @Autowired
    public MessageToModelMapper(final List<MessageMapper<?, ?>> mappers) {
        this.registry = mappers.stream()
            .collect(Collectors.toMap(MessageMapper::messageType, m -> m));
    }

    @Override
    @SuppressWarnings("unchecked")
    public MessageModel apply(final Message message) {
        if (message == null) return null;

        return ((MessageMapper<Message, MessageModel>)
                registry.get(message.getClass()))
                .apply(message);
    }
}
