package com.vaadin.componentfactory.lookupfield;

import com.vaadin.componentfactory.lookupfield.bean.Person;
import com.vaadin.componentfactory.lookupfield.service.PersonService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

import java.util.List;

/**
 * Example with a label generator
 * When you update the label generator, the search is done on the generated label
 */
@Route(value = "person-id", layout = MainLayout.class)
public class PersonLabelGeneratorView extends Div {


    public PersonLabelGeneratorView() {
        LookupField<Person> lookupField = new LookupField<>(Person.class);
        List<Person> items = getItems();
        lookupField.setDataProvider(DataProvider.ofCollection(items));
        // you can set the grid size
        lookupField.setGridWidth("80vw");
        lookupField.getGrid().setHeight("70vh");
        lookupField.setItemLabelGenerator(item -> item.getId() + "-" + item.toString());
        lookupField.setHeader("Person Search");
        add(lookupField);
    }

    private List<Person> getItems() {
        PersonService personService = new PersonService();
        return personService.fetchAll();
    }
}
