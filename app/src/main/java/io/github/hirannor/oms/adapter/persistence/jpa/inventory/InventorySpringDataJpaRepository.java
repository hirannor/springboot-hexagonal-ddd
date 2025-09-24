package io.github.hirannor.oms.adapter.persistence.jpa.inventory;


import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

interface InventorySpringDataJpaRepository extends Repository<InventoryModel, Long> {

    InventoryModel save(InventoryModel inventory);

    void deleteByInventoryId(String inventoryId);

    Optional<InventoryModel> findByInventoryId(String inventoryId);

    List<InventoryModel> findAll();

    List<InventoryModel> findAllByProductIdIn(List<String> productIds);

    Optional<InventoryModel> findByProductId(String productId);

    boolean existsByInventoryId(String inventoryId);
}
