package io.github.hirannor.oms.adapter.persistence.jpa.inventory;

import io.github.hirannor.oms.adapter.persistence.jpa.inventory.mapping.InventoryModelToDomainMapper;
import io.github.hirannor.oms.adapter.persistence.jpa.inventory.mapping.InventoryModeller;
import io.github.hirannor.oms.domain.inventory.Inventory;
import io.github.hirannor.oms.domain.inventory.InventoryId;
import io.github.hirannor.oms.domain.inventory.InventoryRepository;
import io.github.hirannor.oms.domain.product.ProductId;
import io.github.hirannor.oms.infrastructure.adapter.DrivenAdapter;
import io.github.hirannor.oms.infrastructure.adapter.PersistenceAdapter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@DrivenAdapter
@PersistenceAdapter
class InventoryJpaRepository implements InventoryRepository {

    private final InventorySpringDataJpaRepository inventories;
    private final Function<InventoryModel, Inventory> mapModelToDomain;

    @Autowired
    InventoryJpaRepository(final InventorySpringDataJpaRepository inventories) {
        this(inventories, new InventoryModelToDomainMapper());
    }

    InventoryJpaRepository(final InventorySpringDataJpaRepository inventories,
                           final Function<InventoryModel, Inventory> mapModelToDomain) {
        this.inventories = inventories;
        this.mapModelToDomain = mapModelToDomain;
    }

    @Override
    public void save(final Inventory inventory) {
        if (inventory == null) throw new IllegalArgumentException("inventory cannot be null");

        final InventoryModel toPersist = inventories.findByInventoryId(inventory.id().asText())
                .orElseGet(InventoryModel::new);

        InventoryModeller.applyChangesFrom(inventory).to(toPersist);
        inventories.save(toPersist);
    }

    @Override
    public List<Inventory> findAll() {
        return inventories.findAll()
                .stream()
                .map(mapModelToDomain)
                .toList();
    }

    @Override
    public List<Inventory> findAllBy(final List<ProductId> products) {
        final List<String> rawIds = products.stream()
                .map(ProductId::value)
                .toList();

        return inventories.findAllByProductIdIn(rawIds)
                .stream()
                .map(mapModelToDomain)
                .toList();
    }

    @Override
    public Optional<Inventory> findBy(final InventoryId id) {
        if (id == null) throw new IllegalArgumentException("InventoryId cannot be null");

        return inventories.findByInventoryId(id.asText())
                .map(mapModelToDomain);
    }

    @Override
    public Optional<Inventory> findBy(final ProductId productId) {
        if (productId == null) throw new IllegalArgumentException("ProductId cannot be null");

        return inventories.findByProductId(productId.asText())
                .map(mapModelToDomain);
    }

    @Override
    public void deleteBy(final InventoryId id) {
        if (id == null) throw new IllegalArgumentException("InventoryId cannot be null");

        inventories.deleteByInventoryId(id.asText());
    }
}
