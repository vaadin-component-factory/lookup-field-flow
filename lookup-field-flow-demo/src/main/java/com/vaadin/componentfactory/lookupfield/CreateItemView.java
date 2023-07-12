package com.vaadin.componentfactory.lookupfield;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.List;

/**
 * Basic example with setItems
 */
@Route(value = "create", layout = MainLayout.class)
public class CreateItemView extends Div {


    public CreateItemView() {
        LookupField<String> lookupField = new LookupField<>();
        List<String> items = Arrays.asList("item1","item2", "item3");
        lookupField.setDataProvider(DataProvider.ofCollection(items));
        lookupField.getGrid().addColumn(s -> s).setHeader("item");
        lookupField.setLabel("Item selector");
        lookupField.addCreateItemListener(e -> Notification.show("Create item clicked"));
        lookupField.setModal(false);
        add(lookupField);
    }

}
