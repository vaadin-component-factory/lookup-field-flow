package com.vaadin.componentfactory.lookupfield.service;

import com.vaadin.componentfactory.lookupfield.bean.Person;
import com.vaadin.componentfactory.lookupfield.bean.PersonFilter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class FilteredPersonService {
    private PersonData personData = new PersonData();

    public List<Person> fetch(int offset, int limit, PersonFilter filter) {
        int end = offset + limit;
        List<Person> collect = personData.getPersons().stream().filter(person -> filter(person, filter)).collect(Collectors.toList());
        int size = collect.size();
        if (size <= end) {
            end = size;
        }
        return collect.subList(offset, end);
    }

    private boolean filter(Person person, PersonFilter filter) {
        boolean result = true;
        if (filter == null || (isEmpty(filter.getLastName()) && isEmpty(filter.getFirstName()))) {
            return true;
        }
        if ((isEmpty(filter.getLastName()) && isEmpty(filter.getFirstName()))) {
            return person.toString().toLowerCase(Locale.ROOT).contains(filter.getFullName().toLowerCase(Locale.ROOT));
        } else {
            return person.getLastName().toLowerCase(Locale.ROOT).contains(filter.getLastName().toLowerCase(Locale.ROOT)) && person.getFirstName().toLowerCase(Locale.ROOT).contains(filter.getFirstName().toLowerCase(Locale.ROOT));
        }
    }

    public int count(PersonFilter filter) {
        return (int) personData.getPersons().stream().filter(person -> filter(person, filter)).count();
    }

    private boolean isEmpty(String field) {
      return field == null || field == ""; 
    }
}