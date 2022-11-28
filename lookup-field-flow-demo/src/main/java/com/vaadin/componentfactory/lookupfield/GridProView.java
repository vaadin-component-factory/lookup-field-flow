package com.vaadin.componentfactory.lookupfield;

import com.vaadin.componentfactory.theme.EnhancedDialogVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.gridpro.ItemUpdater;
import com.vaadin.flow.component.notification.Notification;
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
        grid.setItems(new Thing("Foo"), new Thing("Bar"));
        grid.addEditColumn(item -> item.getValue()).custom(getField(),
                (thing, s) -> thing.setValue(s)).setHeader("Editor");
        grid.addColumn(item -> item.getValue()).setHeader("value");
        add(getField());
        addAndExpand(grid);

    }

    public LookupField<String> getField() {
        LookupField<String> lookupField = new LookupField<>();
        List<String> items = Arrays.asList("Foo", "Bar", "item1","item2", "item3");
        lookupField.setDataProvider(DataProvider.ofCollection(items));
        lookupField.getGrid().addColumn(s -> s).setHeader("item");
        lookupField.setLabel("Item selector");
        lookupField.addThemeVariants(EnhancedDialogVariant.SIZE_MEDIUM);
        lookupField.addValueChangeListener(e -> {
            Notification.show("Changed lookupfield value to " + e.getValue());
        });
        return lookupField;
    }
}
