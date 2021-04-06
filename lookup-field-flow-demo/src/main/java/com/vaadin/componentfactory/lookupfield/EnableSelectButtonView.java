package com.vaadin.componentfactory.lookupfield;

import com.vaadin.componentfactory.lookupfield.bean.Person;
import com.vaadin.componentfactory.lookupfield.service.PersonService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.util.List;

/**
 * Selection button enabled by default
 */
@Route(value = "enable-selection", layout = MainLayout.class)
public class EnableSelectButtonView extends Div {


    public EnableSelectButtonView() {
        LookupField<Person> lookupField = new LookupField<>(Person.class);
        List<Person> items = getItems();
        lookupField.setDataProvider(DataProvider.ofCollection(items));
        lookupField.setGridWidth("900px");
        lookupField.setHeader("Person Search");
        lookupField.setSelectionDisabledIfEmpty(false);
        add(lookupField);
    }

    private List<Person> getItems() {
        PersonService personService = new PersonService();
        return personService.fetchAll();
    }
}
