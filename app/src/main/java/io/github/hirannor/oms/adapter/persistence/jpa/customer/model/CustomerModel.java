package io.github.hirannor.oms.adapter.persistence.jpa.customer.model;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Customer related persistence entity model.
 *
 * @author Mate Karolyi
 */
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

    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;

    @Column(name = "GENDER")
    @Enumerated(EnumType.STRING)
    private GenderModel gender;

    @Column
    private CountryModel country;

    @Column(name = "CITY")
    private String city;

    @Column(name = "POSTAL_CODE")
    private Integer postalCode;

    @Column(name = "STREET_ADDRESS")
    private String streetAddress;

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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(final LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public GenderModel getGender() {
        return gender;
    }

    public void setGender(final GenderModel gender) {
        this.gender = gender;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
    }

}
