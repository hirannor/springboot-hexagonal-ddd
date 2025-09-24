package io.github.hirannor.oms.application.service.inventory;

import io.github.hirannor.oms.application.port.outbox.Outbox;
import io.github.hirannor.oms.application.usecase.inventory.InventoryCreation;
import io.github.hirannor.oms.application.usecase.inventory.StockDeduction;
import io.github.hirannor.oms.application.usecase.inventory.StockReleasing;
import io.github.hirannor.oms.application.usecase.inventory.StockReserving;
import io.github.hirannor.oms.domain.core.valueobject.ProductQuantity;
import io.github.hirannor.oms.domain.inventory.Inventory;
import io.github.hirannor.oms.domain.inventory.InventoryRepository;
import io.github.hirannor.oms.domain.inventory.command.CreateInventory;
import io.github.hirannor.oms.domain.inventory.command.DeductStock;
import io.github.hirannor.oms.domain.inventory.command.ReleaseStock;
import io.github.hirannor.oms.domain.inventory.command.ReserveStock;
import io.github.hirannor.oms.domain.product.ProductId;
import io.github.hirannor.oms.infrastructure.application.ApplicationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationService
class InventoryService implements
        InventoryCreation,
        StockDeduction,
        StockReleasing,
        StockReserving {

    private static final Logger LOGGER = LogManager.getLogger(InventoryService.class);

    private final InventoryRepository inventories;
    private final Outbox outboxes;

    @Autowired
    InventoryService(final InventoryRepository inventories,
                     final Outbox outboxes) {
        this.inventories = inventories;
        this.outboxes = outboxes;
    }

    @Override
    public Inventory create(final CreateInventory cmd) {
        if (cmd == null) throw new IllegalArgumentException("CreateInventory is null");

        LOGGER.info("Start creating inventory for productId={}, initialQuantity={}",
                cmd.productId().asText(),
                cmd.initialQuantity()
        );

        final Inventory inventory = Inventory.create(cmd);
        inventories.save(inventory);

        inventory.events().forEach(outboxes::save);
        inventory.clearEvents();

        LOGGER.info("Created inventory with inventoryId={} for productId={}, availableQuantity={}",
                inventory.id().asText(),
                inventory.productId().asText(),
                inventory.availableQuantity()
        );

        return inventory;
    }

    @Override
    public void reserve(final ReserveStock cmd) {
        if (cmd == null) throw new IllegalArgumentException("ReserveStock is null");

        LOGGER.info("Start reserving stock for {} quantity", cmd.products().size());

        final Map<ProductId, Inventory> inventoriesByProduct = inventoriesByProductId(cmd.products());

        for (final ProductQuantity pq : cmd.products()) {
            final Inventory inv = inventoriesByProduct.get(pq.productId());

            if (inv == null)
                throw new IllegalStateException("No inventory found for productId=" + pq.productId().asText());

            inv.reserve(pq.quantity());
            inventories.save(inv);

            inv.events().forEach(outboxes::save);
            inv.clearEvents();
        }

        LOGGER.info("Stock reserved successfully for {} quantity", cmd.products().size());
    }

    @Override
    public void release(final ReleaseStock cmd) {
        if (cmd == null) throw new IllegalArgumentException("ReleaseStock is null");

        LOGGER.info("Start releasing stock for {} quantity", cmd.products().size());

        final Map<ProductId, Inventory> inventoriesByProduct = inventoriesByProductId(cmd.products());

        for (final ProductQuantity pq : cmd.products()) {
            final Inventory inv = inventoriesByProduct.get(pq.productId());

            if (inv == null)
                throw new IllegalStateException("No inventory found for productId=" + pq.productId().asText());

            inv.release(pq.quantity());
            inventories.save(inv);

            inv.events().forEach(outboxes::save);
            inv.clearEvents();
        }

        LOGGER.info("Stock released successfully for {} quantity", cmd.products().size());
    }

    @Override
    public void deduct(final DeductStock cmd) {
        if (cmd == null) throw new IllegalArgumentException("DeductStock is null");

        LOGGER.info("Start deducting stock for {} quantity", cmd.products().size());

        final Map<ProductId, Inventory> inventoriesByProduct = inventoriesByProductId(cmd.products());

        for (final ProductQuantity pq : cmd.products()) {
            final Inventory inv = inventoriesByProduct.get(pq.productId());

            if (inv == null)
                throw new IllegalStateException("No inventory found for productId=" + pq.productId().asText());

            inv.deduct(pq.quantity());
            inventories.save(inv);

            inv.events().forEach(outboxes::save);
            inv.clearEvents();
        }

        LOGGER.info("Stock deducted successfully for {} quantity", cmd.products().size());
    }

    private Map<ProductId, Inventory> inventoriesByProductId(final List<ProductQuantity> products) {
        final List<ProductId> productIds = products
                .stream()
                .map(ProductQuantity::productId)
                .toList();

        return this.inventories.findAllBy(productIds)
                .stream()
                .collect(Collectors.toMap(Inventory::productId, Function.identity()));
    }
}
