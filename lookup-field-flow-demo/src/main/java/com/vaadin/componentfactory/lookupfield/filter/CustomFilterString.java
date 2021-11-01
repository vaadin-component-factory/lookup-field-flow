package com.vaadin.componentfactory.lookupfield.filter;

import com.vaadin.componentfactory.lookupfield.LookupFieldFilter;
import com.vaadin.componentfactory.lookupfield.LookupFieldFilterAction;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

/**
 * @author jcgueriaud
 */
public class CustomFilterString implements LookupFieldFilter<String> {

    private final HorizontalLayout layout = new HorizontalLayout();
    private final TextField filterField;

    private LookupFieldFilterAction<String> fieldFilterAction;

    public CustomFilterString() {
        layout.setSpacing(false);
        layout.getThemeList().add("spacing-s");
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        filterField = new TextField("filter");
        layout.addAndExpand(filterField);
        Button filter = new Button("Filter");
        filter.addClickListener(e -> {
            if (fieldFilterAction != null) {
                fieldFilterAction.filter(filterField.getValue());
            }
        });
        layout.add(filter);
    }

    @Override
    public Component getComponent() {
        return layout;
    }

    @Override
    public void setFilterAction(LookupFieldFilterAction<String> fieldFilterAction) {
        this.fieldFilterAction = fieldFilterAction;
    }

}
