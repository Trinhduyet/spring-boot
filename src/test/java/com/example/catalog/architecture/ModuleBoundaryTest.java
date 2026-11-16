package com.example.catalog.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;

import org.junit.jupiter.api.Test;

class ModuleBoundaryTest {
    private static final String[] FEATURES = {
        "product", "category", "inventory", "ordering", "audit"
    };

    private final JavaClasses classes =
            new ClassFileImporter()
                    .withImportOption(new ImportOption.DoNotIncludeTests())
                    .importPackages("com.example.catalog");

    @Test
    void applicationMustNotDependOnHttpOrEntityManager() {
        noClasses()
                .that()
                .resideInAnyPackage("..application..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(
                        "..api..", "org.springframework.web..", "org.springframework.http..")
                .check(classes);
        noClasses()
                .that()
                .resideInAnyPackage("..application..")
                .should()
                .dependOnClassesThat()
                .haveFullyQualifiedName("jakarta.persistence.EntityManager")
                .check(classes);
    }

    @Test
    void applicationValuesAndPortsMustNotDependOnAdaptersOrFrameworks() {
        noClasses()
                .that()
                .resideInAnyPackage(
                        "..application.input..", "..application.result..", "..application.port..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(
                        "..api..",
                        "..infrastructure..",
                        "..application.service..",
                        "org.springframework..",
                        "jakarta.persistence..",
                        "jakarta.validation..")
                .check(classes);
    }

    @Test
    void typesMustLiveInTheirResponsibilityPackages() {
        String[][] locations = {
            {"Controller", "..api.controller"},
            {"Request", "..api.request"},
            {"Response", "..api.response"},
            {"Service", "..application.service"},
            {"Input", "..application.input"},
            {"Result", "..application.result"},
            {"Entity", "..infrastructure.persistence.entity"},
            {"Repository", "..infrastructure.persistence.repository"},
            {"Exception", "..domain.exception"}
        };
        for (String[] location : locations) {
            classes()
                    .that()
                    .haveSimpleNameEndingWith(location[0])
                    .should()
                    .resideInAPackage(location[1])
                    .check(classes);
        }
    }

    @Test
    void apiMustNotDependOnInfrastructure() {
        ArchRule rule =
                noClasses()
                        .that()
                        .resideInAnyPackage("..api..")
                        .should()
                        .dependOnClassesThat()
                        .resideInAnyPackage("..infrastructure..");
        rule.check(classes);
    }

    @Test
    void domainMustRemainIndependentOfOtherLayersAndFrameworks() {
        ArchRule rule =
                noClasses()
                        .that()
                        .resideInAnyPackage("..domain..")
                        .should()
                        .dependOnClassesThat()
                        .resideInAnyPackage(
                                "..api..",
                                "..application..",
                                "..infrastructure..",
                                "org.springframework..",
                                "jakarta.persistence..");
        rule.check(classes);
    }

    @Test
    void businessFeaturesMustNotHaveDependencyCycles() {
        slices().matching("com.example.catalog.(*)..").should().beFreeOfCycles().check(classes);
    }

    @Test
    void featuresMustNotDependOnAnotherFeaturesInfrastructure() {
        for (String feature : FEATURES) {
            String[] foreignInfrastructure =
                    java.util.Arrays.stream(FEATURES)
                            .filter(other -> !other.equals(feature))
                            .map(other -> "com.example.catalog." + other + ".infrastructure..")
                            .toArray(String[]::new);
            ArchRule rule =
                    noClasses()
                            .that()
                            .resideInAnyPackage("com.example.catalog." + feature + "..")
                            .should()
                            .dependOnClassesThat()
                            .resideInAnyPackage(foreignInfrastructure);
            rule.check(classes);
        }
    }
}
