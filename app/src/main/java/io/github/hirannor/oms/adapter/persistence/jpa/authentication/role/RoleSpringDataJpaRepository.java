package io.github.hirannor.oms.adapter.persistence.jpa.authentication.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleSpringDataJpaRepository extends JpaRepository<RoleModel, Long> {

    /**
     * Finds a RoleModel by its name.
     *
     * @param name the name of the role (e.g., "CUSTOMER", "ADMIN")
     * @return optional RoleModel if found
     */
    Optional<RoleModel> findByName(String name);
}
