package hu.hirannor.hexagonal.infrastructure.event;


import java.lang.annotation.*;

/**
 * @author Mate Karolyi
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventPublisher {
}
