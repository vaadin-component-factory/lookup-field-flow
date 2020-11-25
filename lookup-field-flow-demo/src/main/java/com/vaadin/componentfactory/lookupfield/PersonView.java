package com.vaadin.componentfactory.lookupfield;

import com.vaadin.componentfactory.lookupfield.bean.Person;
import com.vaadin.componentfactory.lookupfield.service.PersonService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

import java.util.List;

/**
 * Basic example with a person with a grid generated automatically
 * with all the fields of the person
 */
@Route(value = "person", layout = MainLayout.class)
public class PersonView extends Div {


    public PersonView() {
        LookupField<Person> lookupField = new LookupField<>(Person.class);
        List<Person> items = getItems();
        lookupField.setDataProvider(DataProvider.ofCollection(items));
        lookupField.setGridWidth("900px");
        lookupField.setHeader("Person Search");
        add(lookupField);
    }

    private List<Person> getItems() {
        PersonService personService = new PersonService();
        return personService.fetchAll();
    }
}
