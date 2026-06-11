package com.vaadin.componentfactory.lookupfield.it;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import com.vaadin.componentfactory.lookupfield.it.support.SpringPlaywrightIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ErroNotificationOnEmptySelectViewPlainIT extends SpringPlaywrightIT {

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
        Locator dialog = fillComboBoxAndOpenDialog(null);
        Locator select = dialog.locator("> [slot='footer']")
                .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Select"));
        assertThat(select).isEnabled();
        select.click(); // click open a notification but doesn't close the dialog
        assertThat(dialog).hasAttribute("opened", "");
        // select the first row by clicking its first body cell
        Locator grid = dialog.locator("vaadin-grid");
        grid.getByRole(AriaRole.GRIDCELL).first().click();
        assertThat(select).isEnabled();
        select.click();
        assertThat(dialog).hasAttribute("opened", "");
        Locator notification = page.locator("vaadin-notification-card[slot]")
                .filter(new Locator.FilterOptions().setHasText("Empty selection"));
        assertThat(notification).isVisible();
    }

    private Locator fillComboBoxAndOpenDialog(String value) {
        if (value != null) {
            page.getByLabel("Item selector").fill(value);
            page.locator("vaadin-combo-box-overlay")
                    .getByRole(AriaRole.OPTION, new Locator.GetByRoleOptions().setName(value))
                    .click();
        }
        page.getByLabel("Click to open the search dialog").click();
        Locator dialog = page.getByRole(AriaRole.DIALOG)
                .and(page.locator("vaadin-dialog"));
        assertThat(dialog).hasAttribute("opened", "");
        return dialog;
    }
}
