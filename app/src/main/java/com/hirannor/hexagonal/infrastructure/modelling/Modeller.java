package com.hirannor.hexagonal.infrastructure.modelling;

@FunctionalInterface
public interface Modeller<TO_MUTATE> {
    TO_MUTATE to(TO_MUTATE from);
}
