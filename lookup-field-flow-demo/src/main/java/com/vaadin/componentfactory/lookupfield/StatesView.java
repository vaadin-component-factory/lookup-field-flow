package com.vaadin.componentfactory.lookupfield;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.List;

/**
 * Basic example with setItems
 */
@Route(value = "states", layout = MainLayout.class)
public class StatesView extends VerticalLayout {


    public StatesView() {
        LookupField<String> enabledLookupField = createLookupField();
        enabledLookupField.setLabel("Enabled Field");
        add(enabledLookupField);
        LookupField<String> disabledLookupField = createLookupField();
        disabledLookupField.setLabel("Disabled Field");
        add(disabledLookupField);
        disabledLookupField.setEnabled(false);
        LookupField<String> readOnlyLookupField = createLookupField();
        readOnlyLookupField.setLabel("Read-only Field");
        readOnlyLookupField.setReadOnly(true);
        add(readOnlyLookupField);
        LookupField<String> invalidLookupField = createLookupField();
        invalidLookupField.setLabel("Invalid Field");
        invalidLookupField.setInvalid(true);
        add(invalidLookupField);
    }

    private LookupField<String> createLookupField() {
        LookupField<String> lookupField = new LookupField<>();
        List<String> items = Arrays.asList("item1", "item2", "item3");
        lookupField.setDataProvider(DataProvider.ofCollection(items));
        lookupField.getGrid().addColumn(s -> s).setHeader("item");
        return lookupField;
    }


}