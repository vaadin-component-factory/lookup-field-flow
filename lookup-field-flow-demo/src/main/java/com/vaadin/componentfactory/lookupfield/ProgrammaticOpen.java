package com.vaadin.componentfactory.lookupfield;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.List;

@Route(value = "open", layout = MainLayout.class)
public class ProgrammaticOpen extends HorizontalLayout {


    int count = 0;
    public ProgrammaticOpen() {
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
        lookupField.addValueChangeListener(e -> {
            add(new Span(++count + ". Value changed to '" + e.getValue() + "'" ));
        });
        add(lookupField, new Button("Open dropdown", e -> lookupField.open()));
        lookupField.open();

    }

}
