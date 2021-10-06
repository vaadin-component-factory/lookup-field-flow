package com.vaadin.componentfactory.lookupfield.service;

import com.vaadin.componentfactory.lookupfield.bean.Person;
import com.vaadin.componentfactory.lookupfield.bean.PersonFilter;

import java.util.List;
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
        if (filter == null || (filter.getLastName() == null && filter.getFirstName() == null)) {
            return result;
        }
        if (filter.getLastName() != null) {
            if (person.getLastName().contains(filter.getLastName())) {
                return true;
            }
        }

        if (filter.getFirstName() != null) {
            if (person.getFirstName().contains(filter.getLastName())) {
                return true;
            }
        }
        return false;
    }

    public int count(PersonFilter filter) {
        return (int) personData.getPersons().stream().filter(person -> filter(person, filter)).count();
    }

}