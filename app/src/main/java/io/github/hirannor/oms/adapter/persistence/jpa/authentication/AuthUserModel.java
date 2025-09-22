package io.github.hirannor.oms.adapter.persistence.jpa.authentication;

import io.github.hirannor.oms.adapter.persistence.jpa.authentication.role.RoleModel;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "EC_AUTH_USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    private Set<RoleModel> roles = new HashSet<>();

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

    public Set<RoleModel> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleModel> roles) {
        this.roles = roles;
    }
}
