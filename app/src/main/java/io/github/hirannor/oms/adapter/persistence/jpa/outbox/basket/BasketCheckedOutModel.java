package io.github.hirannor.oms.adapter.persistence.jpa.outbox.basket;


import io.github.hirannor.oms.adapter.persistence.jpa.outbox.DomainEventModel;

import java.time.Instant;
import java.util.List;

public class BasketCheckedOutModel implements DomainEventModel {

    private String eventId;
    private String customerId;
    private List<BasketItemModel> items;
    private Instant occurredAt;

    public BasketCheckedOutModel() {
    }

    public BasketCheckedOutModel(final String eventId,
                                 final String customerId,
                                 final List<BasketItemModel> items,
                                 final Instant occurredAt) {
        this.eventId = eventId;
        this.customerId = customerId;
        this.items = items;
        this.occurredAt = occurredAt;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(final String eventId) {
        this.eventId = eventId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(final String customerId) {
        this.customerId = customerId;
    }

    public List<BasketItemModel> getItems() {
        return items;
    }

    public void setItems(final List<BasketItemModel> items) {
        this.items = items;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(final Instant occurredAt) {
        this.occurredAt = occurredAt;
    }

    @Override
    public String eventId() {
        return this.eventId;
    }
}
