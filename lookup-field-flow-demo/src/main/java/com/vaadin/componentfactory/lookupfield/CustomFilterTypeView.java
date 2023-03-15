package com.vaadin.componentfactory.lookupfield;

import com.vaadin.componentfactory.lookupfield.bean.Address;
import com.vaadin.componentfactory.lookupfield.bean.Person;
import com.vaadin.componentfactory.lookupfield.bean.PersonFilter;
import com.vaadin.componentfactory.lookupfield.filter.CustomFilterPerson;
import com.vaadin.componentfactory.lookupfield.service.FilteredPersonService;
import com.vaadin.componentfactory.lookupfield.service.PersonService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.List;

/**
 * Basic example with a binder and field validation
 */
@Route(value = "custom-filter-type", layout = MainLayout.class)
public class CustomFilterTypeView extends VerticalLayout {

    private FilteredPersonService personService = new FilteredPersonService();

    public CustomFilterTypeView() {
        CustomFilterLookupField<Person, PersonFilter> lookupField = new CustomFilterLookupField<>( fullName -> new PersonFilter(fullName), p -> p.getLastName());
        DataProvider<Person, PersonFilter> dataProvider =
                DataProvider.fromFilteringCallbacks(
                        // First callback fetches items based on a query
                        query -> {
                            // The index of the first item to load
                            int offset = query.getOffset();

                            // The number of items to load
                            int limit = query.getLimit();

                            List<Person> persons = personService
                                    .fetch(offset, limit, query.getFilter().orElse(null));

                            return persons.stream();
                        },
                        // Second callback fetches the total number of items currently in the Grid.
                        // The grid can then use it to properly adjust the scrollbars.
                        query -> personService.count(query.getFilter().orElse(null)));

        lookupField.setDataProvider(dataProvider);
        lookupField.setFilter(new CustomFilterPerson());
        lookupField.getGrid().addColumn(s -> s).setHeader("item");
        lookupField.setLabel("Select one item");
        lookupField.addThemeVariants(LookupFieldVariant.SIZE_MEDIUM);
        add(lookupField);

        CustomFilterMultiSelectLookupField<Person, PersonFilter> multipleLookupField =
                new CustomFilterMultiSelectLookupField<>( fullName -> new PersonFilter(fullName), p -> p.getLastName());


        multipleLookupField.setDataProvider(dataProvider);
        multipleLookupField.setFilter(new CustomFilterPerson());
        multipleLookupField.getGrid().addColumn(s -> s).setHeader("item");
        multipleLookupField.setLabel("Select Multiple items");
        multipleLookupField.addThemeVariants(LookupFieldVariant.SIZE_MEDIUM);
        add(multipleLookupField);
    }
}
