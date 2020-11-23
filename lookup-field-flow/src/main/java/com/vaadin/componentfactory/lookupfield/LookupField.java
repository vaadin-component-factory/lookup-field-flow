package com.vaadin.componentfactory.lookupfield;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.HasFilterableDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.function.SerializableFunction;

import java.util.Collection;
import java.util.Objects;

@Uses(value = Dialog.class)
@Uses(value = Icon.class)
@Uses(value = TextField.class)
@Uses(value = Button.class)
@Tag("vcf-lookup-field")
@JsModule("./vcf-lookup-field.js")
public class LookupField<T> extends Div implements HasFilterableDataProvider<T, String> {

    private static final String FIELD_SLOT_NAME = "field";
    private static final String GRID_SLOT_NAME = "grid";
    private static final String SLOT_KEY = "slot";

    private Grid<T> grid;
    private ComboBox<T> comboBox;
    private ConfigurableFilterDataProvider<T, Void, String> gridDataProvider;

    public LookupField() {
        this(new Grid<>(), new ComboBox<>());
    }

    public LookupField(Class<T> beanType) {
        this(new Grid<>(beanType), new ComboBox<>());
    }

    public LookupField(Grid<T> grid, ComboBox<T> comboBox) {
        setGrid(grid);
        setComboBox(comboBox);
    }

    /**
     * Sets the grid
     *
     * @param grid the grid
     */
    public void setGrid(Grid<T> grid) {
        Objects.requireNonNull(grid, "Grid cannot be null");

        if (this.grid != null && this.grid.getElement().getParent() == getElement()) {
            this.grid.getElement().removeFromParent();
        }

        this.grid = grid;
        grid.getElement().setAttribute(SLOT_KEY, GRID_SLOT_NAME);

        // It might already have a parent e.g when injected from a template
        if (grid.getElement().getParent() == null) {
            getElement().appendChild(grid.getElement());
        }
    }

    /**
     * Sets the comboBox
     *
     * @param comboBox the comboBox
     */
    public void setComboBox(ComboBox<T> comboBox) {
        Objects.requireNonNull(comboBox, "ComboBox cannot be null");

        if (this.comboBox != null && this.comboBox.getElement().getParent() == getElement()) {
            this.comboBox.getElement().removeFromParent();
        }
        comboBox.setClearButtonVisible(true);
        comboBox.setAllowCustomValue(true);

        this.comboBox = comboBox;
        comboBox.getElement().setAttribute(SLOT_KEY, FIELD_SLOT_NAME);

        // It might already have a parent e.g when injected from a template
        if (comboBox.getElement().getParent() == null) {
            getElement().appendChild(comboBox.getElement());
        }
    }

    @Override
    public void setItems(Collection<T> items) {
        comboBox.setItems(items);
        grid.setItems(items);
    }

    public void setItems(ComboBox.ItemFilter<T> itemFilter, Collection<T> items) {
        ListDataProvider<T> listDataProvider = DataProvider.ofCollection(items);

        setDataProvider(itemFilter, listDataProvider);
    }

    public void setDataProvider(ListDataProvider<T> listDataProvider) {
        ComboBox.ItemFilter<T> defaultItemFilter = (item, filterText) ->
            comboBox.getItemLabelGenerator().apply(item).toLowerCase(getLocale())
            .contains(filterText.toLowerCase(getLocale()));

        setDataProvider(defaultItemFilter, listDataProvider);
    }

    public void setDataProvider(ComboBox.ItemFilter<T> itemFilter,
                                ListDataProvider<T> listDataProvider) {
        Objects.requireNonNull(listDataProvider,
            "List data provider cannot be null");

        setDataProvider(listDataProvider,
            filterText -> item -> itemFilter.test(item, filterText));
    }

    public <C> void setDataProvider(DataProvider<T, C> dataProvider,
                                    SerializableFunction<String, C> filterConverter) {
        Objects.requireNonNull(dataProvider, "data provider cannot be null");
        comboBox.setDataProvider(dataProvider, filterConverter);
        gridDataProvider = dataProvider.withConvertedFilter(filterConverter).withConfigurableFilter();
        grid.setDataProvider(gridDataProvider);
    }

    /**
     *
     * @return the internal grid
     */
    public Grid<T> getGrid() {
        return grid;
    }

    /**
     *
     * @return the internal field
     */
    public ComboBox<T> getComboBox() {
        return comboBox;
    }

    /**
     * Copy the selected value of the grid into the field
     */
    @ClientCallable
    private void copyFieldValueFromGrid() {
        grid.getSelectedItems().stream().findFirst().ifPresent(comboBox::setValue);
    }

    /**
     * Copy the selected value of the field into the grid
     */
    @ClientCallable
    private void copyFieldValueToGrid() {
        grid.select(comboBox.getValue());
    }

    /**
     * Filter the grid, todo to change
     * @param filter
     */
    @ClientCallable
    private void filterGrid(String filter) {
        if (filter != null) {
            gridDataProvider.setFilter(filter);
        }
    }

    /**
     * Sets the item label generator that is used to produce the strings shown
     * in the combo box for each item. By default,
     * {@link String#valueOf(Object)} is used.
     * <p>
     *
     * @param itemLabelGenerator
     *            the item label provider to use, not null
     */
    public void setItemLabelGenerator(ItemLabelGenerator<T> itemLabelGenerator) {
        comboBox.setItemLabelGenerator(itemLabelGenerator);
    }

    /**
     * Set the width of the grid
     *
     * @param width the width to set, may be {@code null}
     */
    public void setGridWidth(String width) {
        grid.setWidth(width);
    }

    public void setDialogTitle(String title) {
        getElement().setAttribute("title", title);
    }

    /**
     * Set the label of the field
     *
     * @param label label of the field
     */
    public void setLabel(String label) {
        comboBox.setLabel(label);
    }
}
