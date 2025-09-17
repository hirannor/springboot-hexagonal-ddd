package hu.hirannor.hexagonal.infrastructure.application;

import java.lang.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
@Transactional(
    propagation = Propagation.REQUIRES_NEW,
    isolation = Isolation.REPEATABLE_READ
)
public @interface ApplicationService {
}
