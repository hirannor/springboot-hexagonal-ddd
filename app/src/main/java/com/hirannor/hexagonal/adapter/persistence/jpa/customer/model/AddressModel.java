package com.hirannor.hexagonal.adapter.persistence.jpa.customer.model;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "EC_ADDRESS")
public class AddressModel {

    private static final int ALLOCATION_SIZE = 5;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "address_seq"
    )
    @SequenceGenerator(
            name = "address_seq",
            sequenceName = "address_seq",
            allocationSize = ALLOCATION_SIZE
    )
    private Long id;

    @ManyToMany(mappedBy = "addresses")
    private List<CustomerModel> customer;

    @Column
    @Enumerated(EnumType.STRING)
    private CountryModel country;

    @Column(name = "CITY")
    private String city;

    @Column(name = "POSTAL_CODE")
    private Integer postalCode;

    @Column(name = "STREET_ADDRESS")
    private String streetAddress;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public CountryModel getCountry() {
        return country;
    }

    public void setCountry(final CountryModel country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(final Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(final String streetAddress) {
        this.streetAddress = streetAddress;

    }

    public List<CustomerModel> getCustomer() {
        return customer;
    }

    public void setCustomer(final List<CustomerModel> customer) {
        this.customer = customer;
    }
}
