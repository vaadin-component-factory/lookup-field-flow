package com.vaadin.componentfactory.lookupfield.service;

import com.vaadin.componentfactory.lookupfield.bean.Person;
import com.vaadin.componentfactory.lookupfield.bean.PersonFilter;
import org.apache.commons.lang3.StringUtils;

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
        if (filter == null || (StringUtils.isEmpty(filter.getLastName()) && StringUtils.isEmpty(filter.getFirstName()))) {
            return true;
        }
        if ((StringUtils.isEmpty(filter.getLastName()) && StringUtils.isEmpty(filter.getFirstName()))) {
            return person.toString().contains(filter.getFullName());
        } else {
            return person.getLastName().contains(filter.getLastName()) && person.getFirstName().contains(filter.getFirstName());
        }
    }

    public int count(PersonFilter filter) {
        return (int) personData.getPersons().stream().filter(person -> filter(person, filter)).count();
    }

}