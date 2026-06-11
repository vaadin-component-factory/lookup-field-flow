package com.vaadin.componentfactory.lookupfield.it;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * A dummy view used by the integration tests: a title heading and a text field.
 */
@Route("dummy")
@PageTitle("Dummy View")
public class DummyView extends VerticalLayout {

    public DummyView() {
        H2 title = new H2("Dummy View");
        TextField nameField = new TextField("Name");
        add(title, nameField);
    }
}
