package com.vaadin.componentfactory.lookupfield;

/*
 * #%L
 * lookup-field-flow
 * %%
 * Copyright (C) 2020 Vaadin Ltd
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

import com.vaadin.componentfactory.EnhancedDialog;
import com.vaadin.componentfactory.theme.EnhancedDialogVariant;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.HasFilterableDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.internal.JsonSerializer;
import com.vaadin.flow.shared.Registration;
import elemental.json.JsonObject;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Server-side component for the {@code vcf-lookup-field} webcomponent.
 *
 * The LookupField is a combination of a combobox and a dialog for advanced search.
 *
 *
 * @param <T> the type of the items to be inserted in the combo box
 */

public class MultiSelectLookupField<T> extends AbstractLookupField<T, Set<T>, MultiselectComboBox<T>, MultiSelectLookupField<T>> {

    public MultiSelectLookupField() {
        this(new Grid<>(), new MultiselectComboBox<>());
    }

    public MultiSelectLookupField(Class<T> beanType) {
        this(new Grid<>(beanType), new MultiselectComboBox<>());
    }

    public MultiSelectLookupField(Grid<T> grid, MultiselectComboBox<T> comboBox) {
        super();
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
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<MultiSelectLookupField<T>, Set<T>>> listener) {
        return comboBox.addValueChangeListener((ValueChangeListener) listener);
    }


    /**
     * Set the comboBox
     *
     * @param comboBox the comboBox
     */
    public void setComboBox(MultiselectComboBox<T> comboBox) {
        Objects.requireNonNull(comboBox, "ComboBox cannot be null");

        if (this.comboBox != null && this.comboBox.getElement().getParent() == getElement()) {
            this.comboBox.getElement().removeFromParent();
        }
        comboBox.setClearButtonVisible(true);
        comboBox.setClearButtonVisible(true);
        comboBox.setAllowCustomValues(true);
        comboBox.setCompactMode(true);
        //comboBox.setAllowCustomValue(true);

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
    public MultiselectComboBox<T> getComboBox() {
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
                filterText -> item -> itemFilter.test(item, filterText));
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
        for (T value : comboBox.getValue()) {
            getGrid().select(value);
        }
    }
}
