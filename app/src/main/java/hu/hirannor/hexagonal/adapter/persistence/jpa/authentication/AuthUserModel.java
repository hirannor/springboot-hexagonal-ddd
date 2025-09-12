package hu.hirannor.hexagonal.adapter.persistence.jpa.authentication;

import jakarta.persistence.*;

@Entity
@Table(name = "EC_AUTH_USER")
public class AuthUserModel {

    private static final int ALLOCATION_SIZE = 5;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "auth_user_seq"
    )
    @SequenceGenerator(
            name = "auth_user_seq",
            sequenceName = "auth_user_seq",
            allocationSize = ALLOCATION_SIZE
    )
    private Long id;


    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;

    @Column(name = "PASSWORD")
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
