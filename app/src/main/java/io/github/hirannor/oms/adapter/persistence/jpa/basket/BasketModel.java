package io.github.hirannor.oms.adapter.persistence.jpa.basket;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "EC_BASKETS")
public class BasketModel {

    private static final int ALLOCATION_SIZE = 5;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "basket_seq"
    )
    @SequenceGenerator(
            name = "basket_seq",
            sequenceName = "basket_seq",
            allocationSize = ALLOCATION_SIZE
    )
    private Long id;

    @Column(name = "BASKET_ID", nullable = false, unique = true)
    private String basketId;

    @Column(name = "CUSTOMER_ID", nullable = false)
    private String customerId;

    @OneToMany(
            mappedBy = "basket",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<BasketItemModel> items = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private BasketStatusModel status;

    public BasketModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBasketId() {
        return basketId;
    }

    public void setBasketId(String basketId) {
        this.basketId = basketId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BasketStatusModel getStatus() {
        return status;
    }

    public void setStatus(final BasketStatusModel status) {
        this.status = status;
    }

    public Set<BasketItemModel> getItems() {
        return items;
    }

    public void setItems(Set<BasketItemModel> items) {
        this.items = items;
    }

    public void addItem(final BasketItemModel item) {
        item.setBasket(this);
        this.items.add(item);
    }
}
