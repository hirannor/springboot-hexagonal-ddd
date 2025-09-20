package io.github.hirannor.oms.adapter.persistence.jpa.authentication;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface AuthenticationSpringDataJpaRepository extends Repository<AuthUserModel, Long> {

    void save(AuthUserModel auth);

    Optional<AuthUserModel> findByEmailAddress(String emailAddress);
}
