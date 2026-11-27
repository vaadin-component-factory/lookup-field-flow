package com.vaadin.componentfactory.lookupfield.it;

import com.microsoft.playwright.Locator;
import com.vaadin.componentfactory.lookupfield.it.support.SpringPlaywrightIT;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.vaadin.addons.dramafinder.element.ButtonElement;
import org.vaadin.addons.dramafinder.element.ComboBoxElement;
import org.vaadin.addons.dramafinder.element.DialogElement;
import org.vaadin.addons.dramafinder.element.GridElement;
import org.vaadin.addons.dramafinder.element.NotificationElement;
import org.vaadin.addons.dramafinder.element.TextFieldElement;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ErroNotificationOnEmptySelectViewIT extends SpringPlaywrightIT {

    @Override
    public String getView() {
        return "error-notification";
    }

    @Test
    void testTitle() {
        assertThat(page).hasTitle("Error Notification View");
    }

    @Test
    void testEmptySelectButtonIsEnabled() {
        DialogElement dialogElement = fillComboBoxAndOpenDialog(null);
        ButtonElement select = ButtonElement.getByText(dialogFooter(), "Select");
        select.assertEnabled();
        select.click(); // click open a notification but doesn't close the dialog
        dialogElement.assertOpen();
        GridElement gridElement = GridElement.get(dialogElement.getContentLocator());
        gridElement.select(0);
        select.assertEnabled();
        select.click();
        dialogElement.assertOpen();
        NotificationElement notificationElement = NotificationElement.getByText(page, "Empty selection");
        notificationElement.assertVisible();
    }

    /**
     * Where the default footer buttons are. They are generated into the light
     * DOM of {@code vcf-lookup-field} and shown in the dialog through a
     * forwarded slot, so they are not inside the {@code vaadin-dialog} element.
     */
    private Locator dialogFooter() {
        return page.locator("vcf-lookup-field > [slot='dialog-footer']");
    }

    private DialogElement fillComboBoxAndOpenDialog(String value) {
        if (value != null) {
            ComboBoxElement.getByLabel(page, "Item selector").selectItem(value);
        }
        new ButtonElement(page.getByLabel("Click to open the search dialog")).click();
        DialogElement dialogElement = new DialogElement(page);
        dialogElement.assertOpen();
        return dialogElement;
    }
}
