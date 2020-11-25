package com.vaadin.componentfactory.lookupfield;

import com.vaadin.componentfactory.lookupfield.bean.Person;
import com.vaadin.componentfactory.lookupfield.service.PersonService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.List;

/**
 * Basic example with setItems
 */
@Route(value = "i18n", layout = MainLayout.class)
public class I18nView extends Div {


    public I18nView() {
        LookupField<String> lookupField = new LookupField<>();
        List<String> items = Arrays.asList("item1","item2", "item3");
        lookupField.setDataProvider(DataProvider.ofCollection(items));
        lookupField.getGrid().addColumn(s -> s).setHeader("item");
        lookupField.setLabel("Item selector");
        lookupField.setHeader("utilisateurs");
        lookupField.setI18n(new LookupField.LookupFieldI18n()
            .setSearcharialabel("Cliquer pour ouvrir la fenêtre de recherche")
            .setSelect("Sélectionner")
            .setHeaderprefix("Recherche:")
            .setSearch("Recherche")
            .setHeaderpostfix("")
            .setCancel("Annuler"));
        add(lookupField);
    }

    private List<Person> getItems() {
        PersonService personService = new PersonService();
        return personService.fetchAll();
    }
}
