package com.vaadin.componentfactory.lookupfield;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.List;

/**
 * Styling the dialog from a stylesheet: the dialog parts are exposed on the
 * lookup field with a {@code dialog-} prefix, and the default buttons are in
 * its light DOM.
 */
@Route(value = "styling", layout = MainLayout.class)
@StyleSheet("styles/styling-view.css")
public class StylingView extends VerticalLayout {

    public StylingView() {
        LookupField<String> lookupField = new LookupField<>();
        List<String> items = Arrays.asList("Product", "Service", "HR", "Accounting");
        lookupField.setDataProvider(DataProvider.ofCollection(items));
        lookupField.getGrid().addColumn(s -> s).setHeader("Department");
        lookupField.setLabel("Department");
        lookupField.setHeader("Pick a department");
        lookupField.setId("styled-lookup");
        add(lookupField);
    }
}
