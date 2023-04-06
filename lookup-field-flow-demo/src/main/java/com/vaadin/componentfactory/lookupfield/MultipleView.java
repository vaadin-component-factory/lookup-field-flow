package com.vaadin.componentfactory.lookupfield;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.List;

/**
 * Basic example with setItems
 */
@Route(value = "multiple", layout = MainLayout.class)
public class MultipleView extends Div {


    public MultipleView() {
        MultiSelectLookupField<String> lookupField = new MultiSelectLookupField<>();
        List<String> items = Arrays.asList("item1","item2", "item3");
        lookupField.setDataProvider(DataProvider.ofCollection(items));
        lookupField.getGrid().addColumn(s -> s).setHeader("item");
        lookupField.setLabel("Item selector");
        lookupField.addThemeVariants(LookupFieldVariant.SIZE_MEDIUM);
        lookupField.showSelectedItems(true);
        add(lookupField);
    }

}
