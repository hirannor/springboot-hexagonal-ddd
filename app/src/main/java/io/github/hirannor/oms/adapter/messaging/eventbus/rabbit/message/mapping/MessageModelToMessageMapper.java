package io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.mapping;

import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageModel;
import io.github.hirannor.oms.adapter.messaging.eventbus.rabbit.message.MessageModelMapper;
import io.github.hirannor.oms.infrastructure.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class MessageModelToMessageMapper implements Function<MessageModel, Message> {

    private final Map<Class<? extends MessageModel>, MessageModelMapper<?, ?>> registry;

    @Autowired
    public MessageModelToMessageMapper(final List<MessageModelMapper<?, ?>> mappers) {
        this.registry = mappers.stream()
                .collect(Collectors.toMap(MessageModelMapper::messageType, m -> m));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Message apply(final MessageModel model) {
        if (model == null) return null;

        return ((MessageModelMapper<MessageModel, Message>)
                registry.get(model.getClass()))
                .apply(model);
    }
}
