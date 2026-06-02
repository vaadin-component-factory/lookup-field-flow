package com.vaadin.componentfactory.lookupfield.it;

import com.vaadin.componentfactory.lookupfield.AbstractLookupField;
import com.vaadin.componentfactory.lookupfield.LookupField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.List;

/**
 * A view used by the integration tests: a LookupField backed by three items,
 * mirroring the demo SimpleView but with explicit i18n button labels.
 */
@Route("lookup")
@PageTitle("Lookup View")
public class LookupView extends VerticalLayout {

    public LookupView() {
        LookupField<String> lookupField = new LookupField<>();
        List<String> items = Arrays.asList("item1", "item2", "item3");
        lookupField.setDataProvider(DataProvider.ofCollection(items));
        lookupField.getGrid().addColumn(s -> s).setHeader("item");

        AbstractLookupField.LookupFieldI18n i18n = new AbstractLookupField.LookupFieldI18n();
        i18n.setCancel("Cancel");
        i18n.setSearch("Search");
        i18n.setSelect("Select");
        lookupField.setI18n(i18n);
        lookupField.setLabel("Item selector");

        add(lookupField);
    }
}
