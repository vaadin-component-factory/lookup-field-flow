package com.vaadin.componentfactory.lookupfield;

import com.vaadin.componentfactory.lookupfield.bean.Person;
import com.vaadin.componentfactory.lookupfield.service.PersonService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.List;

/**
 * Basic example with setItems
 */
@Route(value = "", layout = MainLayout.class)
public class SimpleView extends Div {


    public SimpleView() {
        LookupField<String> lookupField = new LookupField<>();
        List<String> items = Arrays.asList("item1","item2", "item3");
        lookupField.setDataProvider(DataProvider.ofCollection(items));
        lookupField.getGrid().addColumn(s -> s).setHeader("item");
        lookupField.setLabel("Item selector");
        add(lookupField);
    }

}
