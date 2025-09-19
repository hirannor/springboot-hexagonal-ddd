package hu.hirannor.hexagonal.adapter.persistence.jpa.authentication.role;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
