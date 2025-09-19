package hu.hirannor.hexagonal.adapter.persistence.jpa.authentication;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface AuthenticationSpringDataJpaRepository extends Repository<AuthUserModel, Long> {

    void save(AuthUserModel auth);

    Optional<AuthUserModel> findByEmailAddress(String emailAddress);
}
