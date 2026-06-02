package com.vaadin.componentfactory.lookupfield.it;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.vaadin.componentfactory.lookupfield.it.support.SpringPlaywrightIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.vaadin.addons.dramafinder.element.TextFieldElement;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DummyViewIT extends SpringPlaywrightIT {

    @Override
    public String getView() {
        return "/dummy";
    }

    @Test
    public void testTitle() {
        assertThat(page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Dummy View"))).isVisible();
    }

    @Test
    public void testTextFieldIsVisible() {
        TextFieldElement nameField = TextFieldElement.getByLabel(page, "Name");
        nameField.assertVisible();
    }
}
