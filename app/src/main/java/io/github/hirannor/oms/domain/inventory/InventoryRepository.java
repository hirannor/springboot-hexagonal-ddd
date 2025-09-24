package io.github.hirannor.oms.domain.inventory;

import io.github.hirannor.oms.domain.product.ProductId;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository {

    void save(Inventory inventory);

    List<Inventory> findAll();

    List<Inventory> findAllBy(List<ProductId> products);

    Optional<Inventory> findBy(InventoryId id);

    Optional<Inventory> findBy(ProductId productId);

    void deleteBy(InventoryId id);
}
