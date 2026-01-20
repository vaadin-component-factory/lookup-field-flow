package com.vaadin.componentfactory.lookupfield;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.List;

/**
 * Basic example with setItems
 */
@Route(value = "simple", layout = MainLayout.class)
public class SimpleView extends VerticalLayout {


    int count = 0;
    public SimpleView() {
        LookupField<String> lookupField = new LookupField<>();
        List<String> items = Arrays.asList("item1","item2", "item3");
        lookupField.setDataProvider(DataProvider.ofCollection(items));
        lookupField.getGrid().addColumn(s -> s).setHeader("item");
        AbstractLookupField.LookupFieldI18n i18n = new AbstractLookupField.LookupFieldI18n();
        i18n.setCancel("cancel");
        i18n.setSearch("search");
        i18n.setSelect("select");
        lookupField.setI18n(i18n);
        lookupField.getI18n().setEmptyselection("Please select one item");
        lookupField.setLabel("Item selector");
        lookupField.addThemeVariants(LookupFieldVariant.SIZE_MEDIUM);
        lookupField.addValueChangeListener(e -> {
            add(new Span(++count + ". Value changed to '" + e.getValue() + "'" ));
        });
        add(lookupField);
    }

}
