package io.github.hirannor.oms.adapter.persistence.jpa.authentication.role;

import jakarta.persistence.*;

@Entity
@Table(name = "EC_ROLE")
public class RoleModel {

    private static final int ALLOCATION_SIZE = 5;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "role_seq"
    )
    @SequenceGenerator(
            name = "role_seq",
            sequenceName = "ROLE_SEQ",
            allocationSize = ALLOCATION_SIZE
    )
    private Long id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    public RoleModel() {
    }

    public RoleModel(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
