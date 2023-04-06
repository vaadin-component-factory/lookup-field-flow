package com.vaadin.componentfactory.lookupfield;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.List;

/**
 * Custom header and footer
 */
@Route(value = "custom-header", layout = MainLayout.class)
public class CustomHeaderView extends Div {

    LookupField<String> lookupField = new LookupField<>();

    public CustomHeaderView() {
        List<String> items = Arrays.asList("item1","item2", "item3");
        lookupField.setDataProvider(DataProvider.ofCollection(items));
        lookupField.getGrid().addColumn(s -> s).setHeader("item");
        lookupField.setLabel("Item selector");
        lookupField.addThemeVariants(LookupFieldVariant.SIZE_MEDIUM);
        lookupField.setHeader("test");
        lookupField.setHeaderComponent(new HorizontalLayout(VaadinIcon.HEADER.create(),new Span("Header Component")));
        HorizontalLayout footer = new HorizontalLayout();
        // push the buttons to the right
        footer.addAndExpand(new Span());
        footer.add(createSelectButton(), createCancelButton());
        lookupField.setFooterComponent(footer);
        add(lookupField);
    }

    public Button createSelectButton() {
        Button selectButton = new Button("Custom Select");
        selectButton.addClickListener(event -> lookupField.footerSelectAction());
        selectButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        lookupField.getGrid().addSelectionListener(e -> selectButton.setEnabled(e.getFirstSelectedItem().isPresent()));
        selectButton.setEnabled(false);
        return selectButton;
    }

    public Button createCancelButton() {
        Button cancelButton = new Button("Custom Cancel");
        cancelButton.addClickListener(event -> lookupField.footerCloseAction());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return cancelButton;
    }
}
