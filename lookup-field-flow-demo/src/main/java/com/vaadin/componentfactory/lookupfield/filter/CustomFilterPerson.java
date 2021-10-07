package com.vaadin.componentfactory.lookupfield.filter;

import com.vaadin.componentfactory.lookupfield.LookupFieldFilter;
import com.vaadin.componentfactory.lookupfield.LookupFieldFilterAction;
import com.vaadin.componentfactory.lookupfield.bean.PersonFilter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

/**
 * @author jcgueriaud
 */
public class CustomFilterPerson implements LookupFieldFilter<PersonFilter> {

    private final HorizontalLayout layout = new HorizontalLayout();
    private final TextField firstNameField;
    private final TextField lastNameField;

    private LookupFieldFilterAction<PersonFilter> fieldFilterAction;

    public CustomFilterPerson() {
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        firstNameField = new TextField("First name");
        lastNameField = new TextField("Last name");
        layout.addAndExpand(firstNameField);
        layout.addAndExpand(lastNameField);
        Button filter = new Button("Filter");
        filter.addClickListener(e -> {
            if (fieldFilterAction != null) {
                fieldFilterAction.filter(new PersonFilter(firstNameField.getValue(), lastNameField.getValue()));
            }
        });
        layout.add(filter);
    }

    @Override
    public Component getComponent() {
        return layout;
    }

    @Override
    public void setFilterAction(LookupFieldFilterAction<PersonFilter> fieldFilterAction) {
        this.fieldFilterAction = fieldFilterAction;
    }

}
