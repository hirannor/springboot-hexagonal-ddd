package io.github.hirannor.oms.architecture;


import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.GeneralCodingRules;
import org.junit.jupiter.api.DisplayName;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "io.github.hirannor.oms")
@DisplayName("GeneralRulesArch")
class GeneralRulesArchTest {

    @DisplayName("should use functions as mappers")
    void shouldUseFunctionsAsMappers(final JavaClasses classes) {
        classes()
                .that().haveSimpleNameEndingWith("Mapper")
                .should().beAssignableTo(Function.class)
                .orShould().beAssignableTo(BiFunction.class)
                .as("Mapper classes should be implementation of Functional interfaces")
                .because("they are transforming information from one notificationType to another")
                .check(classes);
    }

    @ArchTest
    @DisplayName("should not use field injection")
    void shouldNotUseFieldInjection(final JavaClasses classes) {
        GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION
                .as("Classes should not use field injection")
                .because("it's an anti-pattern")
                .check(classes);
    }

    @ArchTest
    @DisplayName("should not use mapstruct")
    void shouldNotUseMapstruct(final JavaClasses classes) {
        noClasses()
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("org.mapstruct..")
                .as("Mapper classes should be functional interface implementation")
                .because(
                        "auto generated mappers as mapstruct which uses lombok not permitted"
                )
                .check(classes);
    }

    @ArchTest
    @DisplayName("should not depend services on services")
    void shouldNotDependServiceOnService(final JavaClasses classes) {
        noClasses()
                .that()
                .areAnnotatedWith(Service.class)
                .or()
                .resideInAPackage("io.github.hirannor.oms.application.service")
                .and()
                .haveSimpleNameNotEndingWith("Test")
                .should()
                .dependOnClassesThat()
                .areAnnotatedWith(Service.class)
                .check(classes);
    }
}
