package com.vaadin.componentfactory.lookupfield;

/*
 * #%L
 * lookup-field-flow
 * %%
 * Copyright (C) 2020 - 2026 Vaadin Ltd
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.shared.Registration;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.Set;

/**
 * Server-side component for the {@code vcf-lookup-field} webcomponent.
 *
 * The LookupField is a combination of a combobox and a dialog for advanced search.
 *
 *
 * @param <T> the type of the items to be inserted in the combo box
 */
public class CustomFilterMultiSelectLookupField<T, FilterType> extends AbstractLookupField<T, Set<T>, MultiSelectComboBox<T>, CustomFilterMultiSelectLookupField<T, FilterType>, FilterType> {

    private static final String SELECTED_SLOT_NAME = "selected";

    private Div selected;

    private Registration selectedListener;

    /**
     * Constructor
     * The converters are used to convert the backend filter to the combobox filter (String)
     * or if you are using setItems
     *
     * @param filterConverter Convert a string to FilterType
     * @param invertedFilterConverter Convert a FilterType to String
     */
    public CustomFilterMultiSelectLookupField(
            SerializableFunction<String, FilterType> filterConverter
            ,SerializableFunction<FilterType, String> invertedFilterConverter) {
        this(new Grid<>(), new MultiSelectComboBox<>(), filterConverter, invertedFilterConverter);
    }

    public CustomFilterMultiSelectLookupField(Class<T> beanType,
                                   SerializableFunction<String, FilterType> filterConverter
            ,SerializableFunction<FilterType, String> invertedFilterConverter) {
        this(new Grid<>(beanType), new MultiSelectComboBox<>(), filterConverter, invertedFilterConverter);
    }

    public CustomFilterMultiSelectLookupField(Grid<T> grid, MultiSelectComboBox<T> comboBox,
                                   SerializableFunction<String, FilterType> filterConverter
            ,SerializableFunction<FilterType, String> invertedFilterConverter) {
        super(filterConverter, invertedFilterConverter);
        setGrid(grid);
        setComboBox(comboBox);
    }

    @Override
    public void setGrid(Grid<T> grid) {
        super.setGrid(grid);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
    }

    @Override
    public void setValue(Set<T> value) {
        comboBox.setValue(value);
    }

    @Override
    public Set<T> getValue() {
        return comboBox.getValue();
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<CustomFilterMultiSelectLookupField<T, FilterType>, Set<T>>> listener) {
        return comboBox.addValueChangeListener((ValueChangeListener) listener);
    }


    /**
     * Set the comboBox
     *
     * @param comboBox the comboBox
     */
    public void setComboBox(MultiSelectComboBox<T> comboBox) {
        Objects.requireNonNull(comboBox, "ComboBox cannot be null");

        if (this.comboBox != null && this.comboBox.getElement().getParent() == getElement()) {
            this.comboBox.getElement().removeFromParent();
        }
        comboBox.setClearButtonVisible(true);
        comboBox.setAllowCustomValue(true);
        comboBox.addCustomValueSetListener(e -> {
            getElement().setProperty("_filterdata", e.getDetail());
        });
        comboBox.addValueChangeListener(e -> {
            getElement().setProperty("_filterdata", "");
        });

        this.comboBox = comboBox;
        comboBox.getElement().setAttribute(SLOT_KEY, FIELD_SLOT_NAME);

        // It might already have a parent e.g when injected from a template
        if (comboBox.getElement().getParent() == null) {
            getElement().appendChild(comboBox.getElement());
        }
    }

    /**
     * @return the internal field
     */
    public MultiSelectComboBox<T> getComboBox() {
        return comboBox;
    }


    @Override
    public void setDataProvider(ListDataProvider<T> listDataProvider) {
        ComboBox.ItemFilter<T> defaultItemFilter = (item, filterText) ->
                comboBox.getItemLabelGenerator().apply(item).toLowerCase(getLocale())
                        .contains(filterText.toLowerCase(getLocale()));

        setDataProvider(defaultItemFilter, listDataProvider);
    }

    /**
     * Sets a list data provider with an item filter as the data provider.
     *
     * @param itemFilter       filter to check if an item is shown when user typed some text
     *                         into the ComboBox
     * @param listDataProvider the list data provider to use, not <code>null</code>
     */
    @Override
    public void setDataProvider(ComboBox.ItemFilter<T> itemFilter,
                                ListDataProvider<T> listDataProvider) {
        Objects.requireNonNull(listDataProvider,
                "List data provider cannot be null");

        setDataProvider(listDataProvider,
                filterText -> item -> itemFilter.test(item, invertedFilterConverter.apply(filterText)));
    }
    /**
     * Sets the item label generator that is used to produce the strings shown
     * in the combo box for each item. By default,
     * {@link String#valueOf(Object)} is used.
     * <p>
     *
     * @param itemLabelGenerator the item label provider to use, not null
     */
    public void setItemLabelGenerator(ItemLabelGenerator<T> itemLabelGenerator) {
        comboBox.setItemLabelGenerator(itemLabelGenerator);
    }
    /**
     * Set the label of the field
     *
     * @param label label of the field
     */
    @Override
    public void setLabel(String label) {
        comboBox.setLabel(label);
    }
    /**
     * Copy the selected value of the grid into the field
     */
    @Override
    @ClientCallable
    protected void copyFieldValueFromGrid() {
        comboBox.setValue(getGrid().getSelectedItems());
    }

    /**
     * Copy the selected value of the field into the grid
     */
    @Override
    @ClientCallable
    protected void copyFieldValueToGrid() {
        getGrid().deselectAll();
        for (T value : comboBox.getValue()) {
            getGrid().select(value);
        }
    }

    /**
     *
     *
     * @param showSelectedItems
     */
    public void showSelectedItems(boolean showSelectedItems) {
        if (this.selected != null && this.selected.getElement().getParent() == getElement()) {
            this.selected.getElement().removeFromParent();
        }
        if (showSelectedItems) {
            this.selected = new Div();
            this.selected.addClassName("selected-text");
            if (selectedListener != null) {
                selectedListener.remove();
            }
            selectedListener = getGrid().addSelectionListener(item -> {
                updateMessage(item.getAllSelectedItems().size());
            });

            updateMessage(getGrid().getSelectedItems().size());
            selected.getElement().setAttribute(SLOT_KEY, SELECTED_SLOT_NAME);

            // It might already have a parent e.g when injected from a template
            if (selected.getElement().getParent() == null) {
                getElement().appendChild(selected.getElement());
            }
        } else {
            this.selected = null;
        }
    }

    private void updateMessage(int nbItems) {
        String message = (getI18n() != null && getI18n().getSelectedText() != null)? getI18n().getSelectedText(): "{0} item(s) selected";
        if (selected != null) {
            this.selected.setText(MessageFormat.format(message, nbItems));
        }
    }

}
