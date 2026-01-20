package com.vaadin.componentfactory.lookupfield;

import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.List;

@Route(value = "gridpro", layout = MainLayout.class)
public class GridProView extends VerticalLayout {

    public static class Thing {

        public Thing(String value) {
            this.value = value;
            this.blah = value.repeat(2).substring(1);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        String value = "";

        public String getBlah() {
            return blah;
        }

        public void setBlah(String blah) {
            this.blah = blah;
        }

        String blah = "";
    }

    public GridProView() {
        setSizeFull();
        GridPro<Thing> grid = new GridPro<>();
        grid.setEnterNextRow(true);
        grid.setItems(new Thing("Foo"), new Thing("Bar"), new Thing("Baz"), new Thing("Quux"));
        grid.addEditColumn(item -> item.getValue()).text(Thing::setValue).setHeader("Value 1");
        grid.addEditColumn(item -> item.getBlah()).text(Thing::setBlah).setHeader("Blah 1");
        grid.addEditColumn(item -> item.getValue()).custom(getField(),
                Thing::setValue).setHeader("Value (custom editor)");
        grid.addColumn(item -> item.getValue()).setHeader("n-e value");
        grid.addColumn(item -> item.getBlah()).setHeader("n-e blah");
        add(getField());
        addAndExpand(grid);

    }

    public LookupField<String> getField() {
        LookupField<String> lookupField = new LookupField<>();
        List<String> items = Arrays.asList("Foo", "Bar", "item1", "item2", "item3");
        lookupField.setDataProvider(DataProvider.ofCollection(items));
        lookupField.getGrid().addColumn(s -> s).setHeader("item");
        lookupField.setLabel("Item selector");
        lookupField.addThemeVariants(LookupFieldVariant.SIZE_MEDIUM);
        return lookupField;
    }
}
