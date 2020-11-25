package com.vaadin.componentfactory.lookupfield;

import com.vaadin.componentfactory.lookupfield.bean.Address;
import com.vaadin.componentfactory.lookupfield.bean.Person;
import com.vaadin.componentfactory.lookupfield.service.PersonData;
import com.vaadin.componentfactory.lookupfield.service.PersonService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.List;

/**
 * Basic example with setItems
 */
@Route(value = "binder", layout = MainLayout.class)
public class BinderView extends Div {

    private Person person = new Person();

    public BinderView() {
        LookupField<Address> lookupField = new LookupField<>();
        Address address1 = new Address("20140", "Turku");
        Address address2 = new Address("10000", "Helsinki");
        Address address3 = new Address("20150", "Turku");
        List<Address> items = Arrays.asList(address1, address2, address3);
        lookupField.setItems(items);
        lookupField.getGrid().addColumn(Address::getPostalCode).setHeader("Postal code");
        lookupField.getGrid().addColumn(Address::getCity).setHeader("City");
        lookupField.setLabel("Address");
        lookupField.setHelperText("helper text for the component");
        lookupField.setWidthFull();
        add(lookupField);

        Binder<Person> personBinder = new Binder<>();
        personBinder.forField(lookupField)
            .asRequired("Address is required")
            .withValidator(e -> "Turku".equals(e.getCity()), "Only Turku is valid")
            .bind(Person::getAddress, Person::setAddress);
        personBinder.setBean(person);
        personBinder.addValueChangeListener(e ->
            Notification.show("Address is now " + person.getAddress())
        );
    }

    private List<Person> getItems() {
        PersonService personService = new PersonService();
        return personService.fetchAll();
    }
}
