package io.github.hirannor.oms.adapter.persistence.jpa.inventory;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "EC_INVENTORY")
public class InventoryModel {

    private static final int ALLOCATION_SIZE = 5;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "inventory_seq"
    )
    @SequenceGenerator(
            name = "inventory_seq",
            sequenceName = "inventory_seq",
            allocationSize = ALLOCATION_SIZE
    )
    private Long id;

    @Column(name = "INVENTORY_ID", nullable = false)
    private String inventoryId;

    @Column(name = "PRODUCT_ID", nullable = false)
    private String productId;

    @Column(name = "AVAILABLE_QUANTITY", nullable = false)
    private int availableQuantity;

    @Column(name = "RESERVED_QUANTITY", nullable = false)
    private int reservedQuantity;

    @Column(name = "CREATED_AT", nullable = false)
    private Instant createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public int getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(int reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
