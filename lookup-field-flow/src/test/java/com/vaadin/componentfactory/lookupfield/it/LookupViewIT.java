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

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LookupViewIT extends SpringPlaywrightIT {

    @Override
    public String getView() {
        return "lookup";
    }

    @Test
    public void testTitle() {
        assertThat(page).hasTitle("Lookup View");
    }

    @Test
    public void testLookupFieldIsVisible() {
        ComboBoxElement lookup = ComboBoxElement.getByLabel(page, "Item selector");
        lookup.assertVisible();
    }

    @Test
    public void testDialogFooterHasSelectButton() {
        DialogElement dialogElement = fillComboBoxWithItem1AndOpenDialog();
        ButtonElement.getByText(dialogElement.getFooterLocator(), "Select").assertVisible();
    }

    @Test
    public void testDialogFooterHasCancelButton() {
        DialogElement dialogElement = fillComboBoxWithItem1AndOpenDialog();
        ButtonElement.getByText(dialogElement.getFooterLocator(), "Cancel").assertVisible();
    }

    @Test
    public void testDialogGridHasOneRow() {
        DialogElement dialogElement = fillComboBoxWithItem1AndOpenDialog();
        // The combobox contains item1 then the filter in dialog is already filled
        TextFieldElement.getByLabel(dialogElement.getContentLocator(), "Search").assertValue("item1");
        Assertions.assertEquals(1, GridElement.get(dialogElement.getContentLocator()).getTotalRowCount());
    }

    @Test
    public void testDialogGridHasThreeRow() {
        DialogElement dialogElement = fillComboBoxAndOpenDialog(null);
        Assertions.assertEquals(3, GridElement.get(dialogElement.getContentLocator()).getTotalRowCount());
    }

    @Test
    public void testDialogFilter() {
        DialogElement dialogElement = fillComboBoxAndOpenDialog(null);
        TextFieldElement.getByLabel(dialogElement.getContentLocator(), "Search").setValue("item3");
        GridElement gridElement = GridElement.get(dialogElement.getContentLocator());
        Assertions.assertEquals(1, gridElement.getTotalRowCount());
    }

    @Test
    public void testSelectButton() {
        DialogElement dialogElement = fillComboBoxAndOpenDialog(null);
        TextFieldElement.getByLabel(dialogElement.getContentLocator(), "Search").setValue("item3");
        GridElement gridElement = GridElement.get(dialogElement.getContentLocator());
        Assertions.assertEquals(1, gridElement.getTotalRowCount());
        gridElement.select(0);
        ButtonElement.getByText(dialogElement.getFooterLocator(), "Select").click();
        dialogElement.assertClosed();
        ComboBoxElement lookup = ComboBoxElement.getByLabel(page, "Item selector");
        lookup.assertValue("item3");
    }

    @Test
    public void testEmptySelectButtonIsDisabled() {
        DialogElement dialogElement = fillComboBoxAndOpenDialog(null);
        ButtonElement.getByText(dialogElement.getFooterLocator(), "Select").assertDisabled();
    }
    /**
     * Fills the combo box with "item1" and clicks the icon search button
     * (located by its accessible name) to open the lookup dialog.
     */
    private DialogElement fillComboBoxWithItem1AndOpenDialog() {
        return fillComboBoxAndOpenDialog("item1");
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
