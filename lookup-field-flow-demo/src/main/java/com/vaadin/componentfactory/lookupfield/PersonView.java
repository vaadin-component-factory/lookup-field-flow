package com.vaadin.componentfactory.lookupfield;

import com.vaadin.componentfactory.lookupfield.bean.Person;
import com.vaadin.componentfactory.lookupfield.service.PersonData;
import com.vaadin.componentfactory.lookupfield.service.PersonService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.List;

/**
 * Basic example with a person
 */
@Route(value = "person", layout = MainLayout.class)
public class PersonView extends Div {


    public PersonView() {
        LookupField<Person> lookupField = new LookupField<>(Person.class);
        List<Person> items = getItems();
        lookupField.setDataProvider(DataProvider.ofCollection(items));
        lookupField.setGridWidth("900px");
        lookupField.setDialogTitle("Person Search");
        add(lookupField);
    }

    private List<Person> getItems() {
        PersonService personService = new PersonService();
        return personService.fetchAll();
    }
}
