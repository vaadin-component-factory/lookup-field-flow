package com.vaadin.componentfactory.lookupfield.it;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Minimal Spring Boot application used only to bootstrap a Vaadin test server
 * for the DramaFinder / Playwright integration tests.
 */

import com.vaadin.flow.theme.aura.Aura;

@StyleSheet(Aura.STYLESHEET)
@SpringBootApplication
public class TestApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
