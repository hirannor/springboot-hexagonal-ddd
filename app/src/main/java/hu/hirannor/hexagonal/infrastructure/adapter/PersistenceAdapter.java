package hu.hirannor.hexagonal.infrastructure.adapter;

import java.lang.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
@Transactional(
    propagation = Propagation.MANDATORY,
    isolation = Isolation.REPEATABLE_READ
)
public @interface PersistenceAdapter { }
