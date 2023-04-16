package com.hirannor.hexagonal.adapter.persistence.jpa.customer.model;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "EC_CUSTOMER")
public class CustomerModel {
    private static final int ALLOCATION_SIZE = 5;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_seq"
    )
    @SequenceGenerator(
            name = "customer_seq",
            sequenceName = "customer_seq",
            allocationSize = ALLOCATION_SIZE
    )
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private String customerId;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "AGE")
    private int age;

    @Column(name = "GENDER")
    private GenderModel gender;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "EC_CUSTOMER_ADDRESS",
            joinColumns = @JoinColumn(
                    name = "customer_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "address_id", referencedColumnName = "id"
            )
    )
    private List<AddressModel> addresses;

    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(final String customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(final int age) {
        this.age = age;
    }

    public GenderModel getGender() {
        return gender;
    }

    public void setGender(final GenderModel gender) {
        this.gender = gender;
    }

    public List<AddressModel> getAddresses() {
        return addresses;
    }

    public void setAddresses(final List<AddressModel> addresses) {
        this.addresses = addresses;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
