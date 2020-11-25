package com.vaadin.componentfactory.lookupfield;

import com.vaadin.componentfactory.EnhancedDialog;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasHelper;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.HasFilterableDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.internal.JsonSerializer;
import com.vaadin.flow.shared.Registration;
import elemental.json.JsonObject;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Uses(value = Icon.class)
@Uses(value = TextField.class)
@Uses(value = Button.class)
@Uses(value = EnhancedDialog.class)
@Tag("vcf-lookup-field")
@JsModule("./vcf-lookup-field.js")
public class LookupField<T> extends Div implements HasFilterableDataProvider<T, String>,
    HasValueAndElement<AbstractField.ComponentValueChangeEvent<LookupField<T>, T>, T>, HasValidation, HasHelper, HasSize {

    private static final String FIELD_SLOT_NAME = "field";
    private static final String GRID_SLOT_NAME = "grid";
    private static final String SLOT_KEY = "slot";

    private LookupFieldI18n i18n;
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
        super();
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
        setDataProvider(DataProvider.ofCollection(items));
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

    /**
     * Sets whether component will open modal or modeless dialog.
     * <p>
     * Note: When dialog is set to be modeless, then it's up to you to provide
     * means for it to be closed (eg. a button that calls {@link Dialog#close()}).
     * The reason being that a modeless dialog allows user to interact with the
     * interface under it and won't be closed by clicking outside or the ESC key.
     *
     * @param modal
     *          {@code false} to enable dialog to open as modeless modal,
     *          {@code true} otherwise.
     */
    public void setModal(boolean modal) {
        getElement().setProperty("modeless", !modal);
    }

    /**
     * Gets whether component is set as modal or modeless dialog.
     *
     * @return  {@code true} if modal dialog (default),
     *          {@code false} otherwise.
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
     * @param draggable
     *          {@code true} to enable dragging of the dialog,
     *          {@code false} otherwise
     */
    public void setDraggable(boolean draggable) {
        getElement().setProperty("draggable", draggable);
    }

    /**
     * Gets whether dialog is enabled to be dragged or not.
     *
     * @return
     *      {@code true} if dragging is enabled,
     *      {@code false} otherwise (default).
     */
    public boolean isDraggable() {
        return getElement().getProperty("draggable", false);
    }

    /**
     * Sets whether dialog can be resized by user or not.
     *
     * @param resizable
     *          {@code true} to enabled resizing of the dialog,
     *          {@code false} otherwise.
     */
    public void setResizable(boolean resizable) {
        getElement().setProperty("resizable", resizable);
    }

    /**
     * Gets whether dialog is enabled to be resized or not.
     *
     * @return
     *      {@code true} if resizing is enabled,
     *      {@code falsoe} otherwiser (default).
     */
    public boolean isResizable() {
        return getElement().getProperty("resizable", false);
    }

    /**
     * Gets the internationalization object previously set for this component.
     * <p>
     * Note: updating the object content that is gotten from this method will
     * not update the lang on the component if not set back using
     * {@link LookupField#setI18n(LookupFieldI18n)}
     *
     * @return the i18n object. It will be <code>null</code>, If the i18n
     *         properties weren't set.
     */
    public LookupFieldI18n getI18n() {
        return i18n;
    }

    /**
     * Sets the internationalization properties for this component.
     *
     * @param i18n
     *            the internationalized properties, not <code>null</code>
     */
    public void setI18n(LookupFieldI18n i18n) {
        Objects.requireNonNull(i18n,
            "The I18N properties object should not be null");
        this.i18n = i18n;
        setI18nWithJS();
    }

    private void setI18nWithJS() {
        runBeforeClientResponse(ui -> {
            JsonObject i18nObject = (JsonObject) JsonSerializer.toJson(i18n);
            for (String key : i18nObject.keys()) {
                getElement().executeJs("this.set('i18n." + key + "', $0)",
                    i18nObject.get(key));
            }
        });
    }

    void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
            .beforeClientResponse(this, context -> command.accept(ui)));
    }

    @Override
    public void setValue(T value) {
        comboBox.setValue(value);
    }

    @Override
    public T getValue() {
        return comboBox.getValue();
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<LookupField<T>, T>> listener) {
        return comboBox.addValueChangeListener((ValueChangeListener) listener);
    }
    @Override
    public boolean isInvalid() {
        return comboBox.isInvalid();
    }

    @Override
    public void setInvalid(boolean invalid) {
        comboBox.setInvalid(invalid);
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
    public String getHelperText() {
        return comboBox.getHelperText();
    }
    @Override
    public void setHelperText(String helperText) {
        comboBox.setHelperText(helperText);
    }
    @Override
    public void setHelperComponent(Component component) {
        comboBox.setHelperComponent(component);
    }

    @Override
    public Component getHelperComponent() {
        return comboBox.getHelperComponent();
    }

    /**
     * The internationalization properties for {@link LookupField}.
     */
    public static class LookupFieldI18n implements Serializable {
        private String select;
        private String cancel;
        private String searcharialabel;
        private String titleprefix;
        private String titlepostfix;
        private String search;

        public String getSearch() {
            return search;
        }

        public LookupFieldI18n setSearch(String search) {
            this.search = search;
            return this;
        }

        public String getSelect() {
            return select;
        }

        public LookupFieldI18n setSelect(String select) {
            this.select = select;
            return this;
        }

        public String getCancel() {
            return cancel;
        }

        public LookupFieldI18n setCancel(String cancel) {
            this.cancel = cancel;
            return this;
        }

        public String getSearcharialabel() {
            return searcharialabel;
        }

        public LookupFieldI18n setSearcharialabel(String searcharialabel) {
            this.searcharialabel = searcharialabel;
            return this;
        }

        public String getTitleprefix() {
            return titleprefix;
        }

        public LookupFieldI18n setTitleprefix(String titleprefix) {
            this.titleprefix = titleprefix;
            return this;
        }

        public String getTitlepostfix() {
            return titlepostfix;
        }

        public LookupFieldI18n setTitlepostfix(String titlepostfix) {
            this.titlepostfix = titlepostfix;
            return this;
        }
    }
}
