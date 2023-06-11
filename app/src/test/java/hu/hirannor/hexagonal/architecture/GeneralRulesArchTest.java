package hu.hirannor.hexagonal.architecture;


import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.GeneralCodingRules;
import org.junit.jupiter.api.DisplayName;
import org.springframework.stereotype.Service;

import java.util.function.*;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "hu.hirannor.hexagonal")
@DisplayName("GeneralRulesArch")
class GeneralRulesArchTest {

    @ArchTest
    void shouldLimitImplementationsOfFunctionalInterfacesToPackageScopeAtMost(
            final JavaClasses classes
    ) {
        classes()
                .that().areAssignableTo(Function.class)
                .or().areAssignableTo(Predicate.class)
                .or().areAssignableTo(Supplier.class)
                .or().areAssignableTo(Consumer.class)
                .should().notBePublic()
                .as("Functional interfaces implementation should not be public")
                .because("they should be kept local")
                .check(classes);
    }

    @ArchTest
    void shouldUseFunctionsAsMappers(final JavaClasses classes) {
        classes()
                .that().haveSimpleNameEndingWith("Mapper")
                .should().beAssignableTo(Function.class)
                .orShould().beAssignableTo(BiFunction.class)
                .as("Mapper classes should be implementation of Functional interfaces")
                .because("they are transforming information from one type to another")
                .check(classes);
    }

    @ArchTest
    void shouldNotUseFieldInjection(final JavaClasses classes) {
        GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION
                .as("Classes should not use field injection")
                .because("it's an anti-pattern")
                .check(classes);
    }

    @ArchTest
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
    void shouldNotDependServiceOnService(final JavaClasses classes) {
        noClasses()
                .that()
                .areAnnotatedWith(Service.class)
                .or()
                .resideInAPackage("hu.hirannor.hexagonal.application.service")
                .and()
                .haveSimpleNameNotEndingWith("Test")
                .should()
                .dependOnClassesThat()
                .areAnnotatedWith(Service.class)
                .check(classes);
    }
}
