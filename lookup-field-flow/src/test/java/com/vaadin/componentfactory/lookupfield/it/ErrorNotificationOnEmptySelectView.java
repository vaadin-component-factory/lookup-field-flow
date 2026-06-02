package com.vaadin.componentfactory.lookupfield.it;

import com.vaadin.componentfactory.lookupfield.AbstractLookupField;
import com.vaadin.componentfactory.lookupfield.LookupField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.List;

@Route("error-notification")
@PageTitle("Error Notification View")
public class ErrorNotificationOnEmptySelectView extends VerticalLayout {

    public ErrorNotificationOnEmptySelectView() {
        LookupField<String> lookupField = new LookupField<>();
        List<String> items = Arrays.asList("item1", "item2", "item3");
        lookupField.setDataProvider(DataProvider.ofCollection(items));
        lookupField.getGrid().addColumn(s -> s).setHeader("item");
        lookupField.setSelectionDisabledIfEmpty(false);

        AbstractLookupField.LookupFieldI18n i18n = new AbstractLookupField.LookupFieldI18n();
        i18n.setCancel("Cancel");
        i18n.setSearch("Search");
        i18n.setSelect("Select");
        i18n.setEmptyselection("Empty selection");
        lookupField.setI18n(i18n);
        lookupField.setLabel("Item selector");

        add(lookupField);
    }
}
