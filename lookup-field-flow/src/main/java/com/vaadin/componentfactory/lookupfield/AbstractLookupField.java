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

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.HasTheme;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.shared.SlotUtils;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.HasFilterableDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.shared.Registration;

import tools.jackson.databind.ObjectMapper;

/**
 * Base class for the server-side components of the {@code vcf-lookup-field} webcomponent.
 * <p>
 * A lookup field is a combination of a combo box and a dialog for advanced search.
 *
 * @param <T>          the type of the items shown in the combo box and grid
 * @param <SelectT>    the value type of the field (a single item or a set of items)
 * @param <ComboboxT>  the type of the combo box used as the field
 * @param <ComponentT> the concrete component type, used for the fluent value-change API
 * @param <FilterType> the type of the filter used to query the data provider
 */
@Uses(value = Icon.class)
@Uses(value = TextField.class)
@Uses(value = Button.class)
@Tag("vcf-lookup-field")
@JsModule("@vaadin-component-factory/vcf-lookup-field")
//@JsModule("./src/vcf-lookup-field.js")
@NpmPackage(value = "@vaadin-component-factory/vcf-lookup-field", version = "6.2.1")
@StyleSheet(value = "lookup-field.css")
public abstract class AbstractLookupField<T, SelectT, ComboboxT extends HasEnabled & HasValidation & HasSize & HasValue<?, SelectT>,
        ComponentT extends AbstractLookupField<T, SelectT, ComboboxT, ComponentT, FilterType>, FilterType> extends Div
        implements HasFilterableDataProvider<T, FilterType>,
        HasValueAndElement<AbstractField.ComponentValueChangeEvent<ComponentT, SelectT>, SelectT>, HasValidation, HasSize, HasTheme {
    /** Name of the slot holding the field (combo box). */
    protected static final String FIELD_SLOT_NAME = "field";
    private static final String GRID_SLOT_NAME = "grid";
    private static final String FILTER_SLOT_NAME = "filter";
    private static final String HEADER_SLOT_NAME = "dialog-header";
    private static final String FOOTER_SLOT_NAME = "dialog-footer";
    /** Attribute key used to assign a component to a slot. */
    protected static final String SLOT_KEY = "slot";
    /** Default message shown when the selection is empty. */
    public static final String DEFAULT_EMPTY_SELECTION = "Please select an item.";
    /** Internationalization properties of this component. */
    private LookupFieldI18n i18n;
    /** Grid shown in the advanced search dialog. */
    private Grid<T> grid;
    /** Combo box used as the field. */
    protected ComboboxT comboBox;
    /** Configurable filter data provider backing the grid. */
    private ConfigurableFilterDataProvider<T, Void, FilterType> gridDataProvider;
    /** Custom filter component, or {@code null} when the default filter is used. */
    private LookupFieldFilter<FilterType> filter;
    /** Action run when the selection is empty and the select button is clicked. */
    private Runnable notificationWhenEmptySelection;
    /** Converts the combo box filter string into the data provider filter type. */
    protected final SerializableFunction<String, FilterType> filterConverter;
    /** Converts the data provider filter type back into a combo box filter string. */
    protected final SerializableFunction<FilterType, String> invertedFilterConverter;

    /**
     * Creates a lookup field with the given filter converters.
     *
     * @param filterConverter         converts the combo box filter string into the
     *                                data provider filter type
     * @param invertedFilterConverter converts the data provider filter type back
     *                                into a combo box filter string
     */
    public AbstractLookupField(SerializableFunction<String, FilterType> filterConverter,
                               SerializableFunction<FilterType, String> invertedFilterConverter) {
        super();
        this.filterConverter = filterConverter;
        this.invertedFilterConverter = invertedFilterConverter;
    }

    /**
     * Set the grid
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
     * Set the comboBox
     *
     * @param comboBox the comboBox
     */

    public abstract void setComboBox(ComboboxT comboBox);

    /**
     * Gets the internal combo box used as the field.
     *
     * @return the internal field
     */
    public abstract ComboboxT getComboBox();

    /**
     * <p>
     * Filtering will use a case insensitive match to show all items where the
     * filter text is a substring of the label displayed for that item, which
     * you can configure with
     * {@link #setItemLabelGenerator(ItemLabelGenerator)}.
     *
     * @param items the data items to display
     */
    @Override
    public void setItems(Collection<T> items) {
        setDataProvider(DataProvider.ofCollection(items));
    }

    /**
     * Sets the items to display, using the given item filter for the combo box.
     *
     * @param itemFilter filter to check if an item is shown when user typed some text
     *                   into the ComboBox
     * @param items      the data items to display
     */
    public void setItems(ComboBox.ItemFilter<T> itemFilter, Collection<T> items) {
        ListDataProvider<T> listDataProvider = DataProvider.ofCollection(items);

        setDataProvider(itemFilter, listDataProvider);
    }

    /**
     * Sets a list data provider as the data provider.
     *
     * @param listDataProvider the list data provider to use, not <code>null</code>
     */
    public abstract void setDataProvider(ListDataProvider<T> listDataProvider);

    /**
     * Sets a list data provider with an item filter as the data provider.
     *
     * @param itemFilter       filter to check if an item is shown when user typed some text
     *                         into the ComboBox
     * @param listDataProvider the list data provider to use, not <code>null</code>
     */
    public abstract void setDataProvider(ComboBox.ItemFilter<T> itemFilter,
                                         ListDataProvider<T> listDataProvider);

    @Override
    public <C> void setDataProvider(DataProvider<T, C> dataProvider,
                                    SerializableFunction<FilterType, C> filterConverter) {
        Objects.requireNonNull(dataProvider, "data provider cannot be null");
        DataProvider<T, String> dataProviderFilteredByString = dataProvider.withConvertedFilter(str -> filterConverter.apply(this.filterConverter.apply(str)));
        if(comboBox instanceof MultiSelectComboBox) {
            ((MultiSelectComboBox<T>)comboBox).setItems(dataProviderFilteredByString);
        } else if(comboBox instanceof ComboBox) {
            ((ComboBox<T>)comboBox).setItems(dataProviderFilteredByString);
        } else {
            throw new RuntimeException("Invalid object passed to the LookupField -> Must be either ComboBox or MultiSelectComboBox");
        }

        gridDataProvider = dataProvider.withConvertedFilter(filterConverter).withConfigurableFilter();
        grid.setDataProvider(gridDataProvider);
    }

    /**
     * Gets the internal grid shown in the advanced search dialog.
     *
     * @return the internal grid
     */
    public Grid<T> getGrid() {
        return grid;
    }


    /**
     * Copy the selected value of the grid into the field
     */
    protected abstract void copyFieldValueFromGrid();

    /**
     * Copy the selected value of the field into the grid
     */
    protected abstract void copyFieldValueToGrid();

    /**
     * Filter the grid
     *
     * @param filter filter text
     */
    @ClientCallable
    private void filterGrid(String filter) {
        // don't filter the grid if the filter is custom
        if (filter != null && this.filter == null) {
            filterServerGrid(filterConverter.apply(filter));
        }
    }

    /**
     * Sets the item label generator that is used to produce the strings shown
     * in the combo box for each item. By default,
     * {@link String#valueOf(Object)} is used.
     *
     * @param itemLabelGenerator the item label provider to use, not null
     */
    public abstract void setItemLabelGenerator(ItemLabelGenerator<T> itemLabelGenerator);

    /**
     * Set the width of the grid
     * Also set a max width to 100%
     *
     * @param width the width to set, may be {@code null}
     */
    public void setGridWidth(String width) {
        grid.setWidth(width);
        grid.setMaxWidth("100%");
    }

    /**
    * Sets the theme variants of this component. This method overwrites any
    * previous set theme variants.
    *
    * @param variants theme variant
    */
   public void setThemeVariants(LookupFieldVariant variants) {
       getElement().getThemeList().clear();
       addThemeVariants(variants);
   }

   /**
    * Adds the theme variants of this component.
    *
    * @param variants theme variant
    */
   public void addThemeVariants(LookupFieldVariant... variants) {
       getElement().getThemeList().addAll(Stream.of(variants).map(LookupFieldVariant::getVariantName).collect(Collectors.toList()));
   }

   /**
     * Set the header of the dialog
     *
     * @param header text for the header of the dialog
     */
    public void setHeader(String header) {
        getElement().setAttribute("header", header);
    }

    /**
     * Set the label of the field
     *
     * @param label label of the field
     */
    public abstract void setLabel(String label);

    /**
     * Sets whether component will open modal or modeless dialog.
     * <p>
     * Note: When dialog is set to be modeless, then it's up to you to provide
     * means for it to be closed (eg. a button that calls {@link Dialog#close()}).
     * The reason being that a modeless dialog allows user to interact with the
     * interface under it and won't be closed by clicking outside or the ESC key.
     *
     * @param modal {@code false} to enable dialog to open as modeless modal,
     *              {@code true} otherwise.
     */
    public void setModal(boolean modal) {
        getElement().setProperty("modeless", !modal);
    }

    /**
     * Gets whether component is set as modal or modeless dialog.
     *
     * @return {@code true} if modal dialog (default),
     * {@code false} otherwise.
     */
    public boolean isModal() {
        return !getElement().getProperty("modeless", false);
    }

    /**
     * Sets whether dialog is enabled to be dragged by the user or not.
     * <p>
     * To allow an element inside the dialog to be dragged by the user
     * (for instance, a header inside the dialog), a class {@code "draggable"}
     * can be added to it (see {@link HasStyle#addClassName(String)}).
     * <p>
     * Note: If draggable is enabled and dialog is opened without first
     * being explicitly attached to a parent, then it won't restore its
     * last position in the case the user closes and opens it again.
     * Reason being that a self attached dialog is removed from the DOM
     * when it's closed and position is not synched.
     *
     * @param draggable {@code true} to enable dragging of the dialog,
     *                  {@code false} otherwise
     */
    public void setDraggable(boolean draggable) {
        getElement().setProperty("draggable", draggable);
    }

    /**
     * Gets whether dialog is enabled to be dragged or not.
     *
     * @return {@code true} if dragging is enabled,
     * {@code false} otherwise (default).
     */
    public boolean isDraggable() {
        return getElement().getProperty("draggable", false);
    }

    /**
     * Sets whether dialog can be resized by user or not.
     *
     * @param resizable {@code true} to enabled resizing of the dialog,
     *                  {@code false} otherwise.
     */
    public void setResizable(boolean resizable) {
        getElement().setProperty("resizable", resizable);
    }

    /**
     * Gets whether dialog is enabled to be resized or not.
     *
     * @return {@code true} if resizing is enabled,
     * {@code false} otherwiser (default).
     */
    public boolean isResizable() {
        return getElement().getProperty("resizable", false);
    }

    /**
     * Sets whether the select button is disabled or send an error when the selection is empty or not.
     *
     * @param defaultselectdisabled {@code true} to disabled the button if no item is disabled,
     *                              {@code false} otherwise.
     */
    public void setSelectionDisabledIfEmpty(boolean defaultselectdisabled) {
        getElement().setProperty("defaultselectdisabled", defaultselectdisabled);
    }

    /**
     * Gets whether the select button is disabled or send an error when the selection is empty or not.
     *
     * @return {@code true} if resizing is enabled,
     * {@code false} otherwiser (default).
     */
    public boolean getSelectionDisabledIfEmpty() {
        return getElement().getProperty("defaultselectdisabled", true);
    }

    /**
     * Gets the internationalization object previously set for this component.
     * <p>
     * Note: updating the object content that is gotten from this method will
     * not update the lang on the component if not set back using
     * {@link LookupField#setI18n(LookupFieldI18n)}
     *
     * @return the i18n object. It will be <code>null</code>, If the i18n
     * properties weren't set.
     */
    public LookupFieldI18n getI18n() {
        return i18n;
    }

    /**
     * Sets the internationalization properties for this component.
     *
     * @param i18n the internationalized properties, not <code>null</code>
     */
    public void setI18n(LookupFieldI18n i18n) {
        Objects.requireNonNull(i18n,
                "The I18N properties object should not be null");
        this.i18n = i18n;
        setI18nWithJS();
    }

    @SuppressWarnings({ "unchecked", "null" })
    private void setI18nWithJS() {
        runBeforeClientResponse(ui -> {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> i18nMap = mapper.convertValue(i18n, Map.class);
            i18nMap.forEach((key, value) -> {
                getElement().executeJs("this.set('i18n." + key + "', $0)", value);
            });
        });
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }



    @Override
    public boolean isInvalid() {
        return comboBox.isInvalid();
    }

    @Override
    public void setInvalid(boolean invalid) {
        this.getElement().setProperty("invalid", invalid);
        comboBox.setInvalid(invalid);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        HasValueAndElement.super.setReadOnly(readOnly);
        comboBox.setReadOnly(readOnly);
    }

    @Override
    public void setEnabled(boolean enabled) {
        comboBox.setEnabled(enabled);
        super.setEnabled(enabled);
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        comboBox.setErrorMessage(errorMessage);
    }

    @Override
    public String getErrorMessage() {
        return comboBox.getErrorMessage();
    }

    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
        HasValueAndElement.super.setRequiredIndicatorVisible(requiredIndicatorVisible);
        comboBox.setRequiredIndicatorVisible(requiredIndicatorVisible);
    }

    /**
     * Set the header with a custom component
     *
     * @param header custom header
     */
    public void setHeaderComponent(Component header) {
        Objects.requireNonNull(header, "Header cannot be null");

        SlotUtils.clearSlot(this, HEADER_SLOT_NAME);
        header.getElement().setAttribute(SLOT_KEY, HEADER_SLOT_NAME);

        // It might already have a parent e.g when injected from a template
        if (header.getElement().getParent() == null) {
            getElement().appendChild(header.getElement());
        }
    }

    /** Registration of the listener that filters the grid from the custom filter. */
    private Registration filterRegistration;

    /**
     * Set the filter with a custom component
     *
     * @param filter custom filter
     */
    public void setFilter(LookupFieldFilter<FilterType> filter) {
        Objects.requireNonNull(filter, "Filter cannot be null");
        Objects.requireNonNull(filter.getComponent(), "Filter component cannot be null");

        if (this.filter != null && this.filter.getComponent() != null && this.filter.getComponent().getElement().getParent() == getElement()) {
            this.filter.getComponent().getElement().removeFromParent();
        }

        this.filter = filter;
        filter.getComponent().getElement().setAttribute(SLOT_KEY, FILTER_SLOT_NAME);
        filter.setFilterAction(value -> {
            ComponentUtil.fireEvent(this, new AbstractLookupField.FilterEvent<>(this, false, value));
        });

        if (filterRegistration != null) {
            filterRegistration.remove();
        }

        filterRegistration = addFilterListener(e -> {
            filterServerGrid(e.getFilterValue());
        });

        // It might already have a parent e.g when injected from a template
        if (filter.getComponent().getElement().getParent() == null) {
            getElement().appendChild(filter.getComponent().getElement());
        }
    }

    private void filterServerGrid(FilterType filter) {
        gridDataProvider.setFilter(filter);
    }

    /**
     * Set the footer with a custom component
     * WARNING: You have to implement your own buttons to select and close the dialog
     *
     * @param footer Custom footer
     */
    public void setFooterComponent(Component footer) {
        Objects.requireNonNull(footer, "Footer cannot be null");

        SlotUtils.clearSlot(this, FOOTER_SLOT_NAME);
        footer.getElement().setAttribute(SLOT_KEY, FOOTER_SLOT_NAME);

        // It might already have a parent e.g when injected from a template
        if (footer.getElement().getParent() == null) {
            getElement().appendChild(footer.getElement());
        }
    }

    /**
     * Select and close the dialog
     */
    public void footerSelectAction() {
        copyFieldValueFromGrid();
    }

    /**
     * Close the dialog
     */
    public void footerCloseAction() {
        getElement().executeJs("$0.__close()", getElement());
    }

    /**
     * Copy the selected value of the field into the grid
     */
    @ClientCallable
    private void openErrorNotification() {
        getNotificationWhenEmptySelection().run();
    }

    private Runnable getNotificationWhenEmptySelection() {
        if (notificationWhenEmptySelection == null) {
            return () -> {
                String emptySelection = (getI18n() == null || getI18n().getEmptyselection() == null || getI18n().getEmptyselection().isBlank()) ? DEFAULT_EMPTY_SELECTION : getI18n().getEmptyselection();
                new Notification(emptySelection, 2000, Notification.Position.TOP_CENTER).open();
            };
        }
        return notificationWhenEmptySelection;
    }

    /**
     * Replace the default notification to an action
     *
     * @param notificationWhenEmptySelection action to run when the selection is empty and the select button is clicked
     */
    public void addEmptySelectionListener(Runnable notificationWhenEmptySelection) {
        this.notificationWhenEmptySelection = notificationWhenEmptySelection;
    }


    /**
     * Add an action when the user filters
     *
     * @param listener the listener to add, not <code>null</code>
     * @return a handle that can be used for removing the listener
     */
    @SuppressWarnings("unchecked")
    public Registration addFilterListener(ComponentEventListener<FilterEvent<FilterType>> listener) {
        return addListener(FilterEvent.class, (ComponentEventListener) listener);
    }

    /**
     * Event fired when the user filters the grid through the custom filter.
     *
     * @param <FILTERTYPE> the type of the filter value
     */
    @DomEvent("vcf-lookup-field-filter-event")
    public static class FilterEvent<FILTERTYPE> extends ComponentEvent<AbstractLookupField> {
        /** The filter value carried by the event. */
        private final FILTERTYPE filterValue;

        /**
         * Creates a new filter event.
         *
         * @param source      the source component
         * @param fromClient  {@code true} if the event originated from the client
         * @param filterValue the filter value
         */
        public FilterEvent(AbstractLookupField source, boolean fromClient, @EventData("event.detail.value") FILTERTYPE filterValue) {
            super(source, fromClient);
            this.filterValue = filterValue;
        }

        /**
         * Gets the filter value carried by this event.
         *
         * @return the filter value
         */
        public FILTERTYPE getFilterValue() {
            return filterValue;
        }
    }

    /**
     * Add an action when the user click on create item
     *
     * @param listener the listener to add, not <code>null</code>
     * @return a handle that can be used for removing the listener
     */
    @SuppressWarnings("unchecked")
    public Registration addCreateItemListener(ComponentEventListener<CreateItemEvent> listener) {
        // show edit button
        setCreateVisible(true);
        return addListener(CreateItemEvent.class, listener);
    }

    /**
     * Sets whether the create button is visible.
     *
     * @param createVisible {@code true} to show the create button,
     *                      {@code false} to hide it
     */
    public void setCreateVisible(boolean createVisible) {
        getElement().setProperty("createhidden", !createVisible);
    }

    /**
     * Event fired when the user clicks the create item button.
     */
    @DomEvent("vcf-lookup-field-create-item-event")
    public static class CreateItemEvent extends ComponentEvent<AbstractLookupField> {
        /**
         * Creates a new create-item event.
         *
         * @param source     the source component
         * @param fromClient {@code true} if the event originated from the client
         */
        public CreateItemEvent(AbstractLookupField source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    /**
     * Opens the combo box overlay.
     */
    public void open() {
        getComboBox().getElement().executeJs("setTimeout(() => $0.open(), 0)");
    }

    /**
     * The internationalization properties for {@link LookupField}.
     */
    public static class LookupFieldI18n implements Serializable {
        /** Text of the select button. */
        private String select;
        /** Text of the cancel button. */
        private String cancel;
        /** Aria label of the search field. */
        private String searcharialabel;
        /** Text shown before the dialog header. */
        private String headerprefix;
        /** Text shown after the dialog header. */
        private String headerpostfix;
        /** Text of the search field. */
        private String search;
        /** Message shown when the selection is empty. */
        private String emptyselection;
        /** Text of the create button. */
        private String create;
        /** Text showing the number of selected items. */
        private String selectedText;

        /**
         * Creates an empty internationalization properties object.
         */
        public LookupFieldI18n() {
        }

        /**
         * Gets the search field text.
         *
         * @return the search field text
         */
        public String getSearch() {
            return search;
        }

        /**
         * Sets the search field text.
         *
         * @param search the search field text
         * @return this instance for method chaining
         */
        public LookupFieldI18n setSearch(String search) {
            this.search = search;
            return this;
        }

        /**
         * Gets the select button text.
         *
         * @return the select button text
         */
        public String getSelect() {
            return select;
        }

        /**
         * Sets the select button text.
         *
         * @param select the select button text
         * @return this instance for method chaining
         */
        public LookupFieldI18n setSelect(String select) {
            this.select = select;
            return this;
        }

        /**
         * Gets the cancel button text.
         *
         * @return the cancel button text
         */
        public String getCancel() {
            return cancel;
        }

        /**
         * Sets the cancel button text.
         *
         * @param cancel the cancel button text
         * @return this instance for method chaining
         */
        public LookupFieldI18n setCancel(String cancel) {
            this.cancel = cancel;
            return this;
        }

        /**
         * Gets the aria label of the search field.
         *
         * @return the search field aria label
         */
        public String getSearcharialabel() {
            return searcharialabel;
        }

        /**
         * Sets the aria label of the search field.
         *
         * @param searcharialabel the search field aria label
         * @return this instance for method chaining
         */
        public LookupFieldI18n setSearcharialabel(String searcharialabel) {
            this.searcharialabel = searcharialabel;
            return this;
        }

        /**
         * Gets the text shown before the dialog header.
         *
         * @return the header prefix text
         */
        public String getHeaderprefix() {
            return headerprefix;
        }

        /**
         * Sets the text shown before the dialog header.
         *
         * @param headerprefix the header prefix text
         * @return this instance for method chaining
         */
        public LookupFieldI18n setHeaderprefix(String headerprefix) {
            this.headerprefix = headerprefix;
            return this;
        }

        /**
         * Gets the text shown after the dialog header.
         *
         * @return the header postfix text
         */
        public String getHeaderpostfix() {
            return headerpostfix;
        }

        /**
         * Sets the text shown after the dialog header.
         *
         * @param headerpostfix the header postfix text
         * @return this instance for method chaining
         */
        public LookupFieldI18n setHeaderpostfix(String headerpostfix) {
            this.headerpostfix = headerpostfix;
            return this;
        }

        /**
         * Gets the message shown when the selection is empty.
         *
         * @return the empty selection message
         */
        public String getEmptyselection() {
            return emptyselection;
        }

        /**
         * Sets the message shown when the selection is empty.
         *
         * @param emptyselection the empty selection message
         * @return this instance for method chaining
         */
        public LookupFieldI18n setEmptyselection(String emptyselection) {
            this.emptyselection = emptyselection;
            return this;
        }

        /**
         * Gets the create button text.
         *
         * @return the create button text
         */
        public String getCreate() {
            return create;
        }

        /**
         * Sets the create button text.
         *
         * @param create the create button text
         * @return this instance for method chaining
         */
        public LookupFieldI18n setCreate(String create) {
            this.create = create;
            return this;
        }

        /**
         * Gets the text showing the number of selected items.
         *
         * @return the selected items text
         */
        public String getSelectedText() {
            return selectedText;
        }

        /**
         * Sets the text showing the number of selected items.
         *
         * @param selectedText the selected items text
         * @return this instance for method chaining
         */
        public LookupFieldI18n setSelectedText(String selectedText) {
            this.selectedText = selectedText;
            return this;
        }
    }
}
