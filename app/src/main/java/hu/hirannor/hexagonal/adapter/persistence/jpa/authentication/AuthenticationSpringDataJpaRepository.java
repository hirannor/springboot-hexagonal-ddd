package hu.hirannor.hexagonal.adapter.persistence.jpa.authentication;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(
    propagation = Propagation.MANDATORY,
    isolation = Isolation.REPEATABLE_READ
)
public interface AuthenticationSpringDataJpaRepository extends Repository<AuthUserModel, Long> {

    void save(AuthUserModel auth);

    Optional<AuthUserModel> findByEmailAddress(String emailAddress);
}
