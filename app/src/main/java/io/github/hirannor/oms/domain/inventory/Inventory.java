package io.github.hirannor.oms.domain.inventory;

import io.github.hirannor.oms.domain.inventory.command.CreateInventory;
import io.github.hirannor.oms.domain.inventory.error.InventoryReleaseFailed;
import io.github.hirannor.oms.domain.inventory.error.InventoryReservationFailed;
import io.github.hirannor.oms.domain.inventory.events.*;
import io.github.hirannor.oms.domain.product.ProductId;
import io.github.hirannor.oms.infrastructure.aggregate.AggregateRoot;
import io.github.hirannor.oms.infrastructure.event.DomainEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Inventory extends AggregateRoot {

    private final InventoryId id;
    private final ProductId productId;
    private int availableQuantity;
    private int reservedQuantity;
    private final List<DomainEvent> events;
    private final Instant createdAt;

    Inventory(final InventoryId id,
              final ProductId productId,
              final int availableQuantity,
              final int reservedQuantity) {
        this.id = Objects.requireNonNull(id);
        this.productId = Objects.requireNonNull(productId);

        if (availableQuantity < 0) throw new IllegalArgumentException("Available quantity cannot be negative");
        if (reservedQuantity < 0) throw new IllegalArgumentException("Reserved quantity cannot be negative");

        this.availableQuantity = availableQuantity;
        this.reservedQuantity = reservedQuantity;
        this.events = new ArrayList<>();
        this.createdAt = Instant.now();
    }

    public static InventoryBuilder empty() {
        return new InventoryBuilder();
    }

    public static Inventory create(final CreateInventory cmd) {
        final Inventory createdInventory = empty()
                .id(cmd.inventoryId())
                .productId(cmd.productId())
                .availableQuantity(cmd.initialQuantity())
                .reservedQuantity(0)
                .assemble();

        createdInventory.events.add(InventoryCreated.record(createdInventory.id, createdInventory.productId, createdInventory.availableQuantity));
        return createdInventory;
    }

    public InventoryId id() {
        return id;
    }

    public ProductId productId() {
        return productId;
    }

    public int availableQuantity() {
        return availableQuantity;
    }

    public int reservedQuantity() {
        return reservedQuantity;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public void reserve(final int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Reserve quantity must be positive");

        if (freeToSell() < quantity) throw new InventoryReservationFailed("Not enough stock to reserve");

        this.reservedQuantity += quantity;
        events.add(StockReserved.record(id, productId, quantity, reservedQuantity, freeToSell()));
    }

    public void release(final int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Release quantity must be positive");

        if (quantity > reservedQuantity) throw new InventoryReleaseFailed("Cannot release more than reserved");

        this.reservedQuantity -= quantity;
        events.add(StockReleased.record(id, productId, quantity, reservedQuantity, freeToSell()));
    }

    public void deduct(final int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Deduct quantity must be positive");

        if (quantity > reservedQuantity || quantity > availableQuantity) {
            events.add(StockDeductionFailed.record(
                    this.id,
                    this.productId,
                    quantity,
                    reservedQuantity,
                    availableQuantity)
            );
            return;
        }

        this.availableQuantity -= quantity;
        this.reservedQuantity -= quantity;

        events.add(StockDeducted.record(id, productId, quantity, availableQuantity, reservedQuantity));
    }

    public int freeToSell() {
        return availableQuantity - reservedQuantity;
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
