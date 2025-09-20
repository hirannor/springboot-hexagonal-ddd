package io.github.hirannor.oms.infrastructure.modelling;

/**
 * Interface to create object mutators with.
 *
 * @param <TO_MUTATE> Class notificationType of to perform mutations on
 * @author Mate Karolyi
 */
@FunctionalInterface
public interface Modeller<TO_MUTATE> {

    /**
     * @param from object to apply mutations to
     * @return mutated object
     */
    TO_MUTATE to(TO_MUTATE from);
}
