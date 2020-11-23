package com.vaadin.componentfactory.lookupfield.service;

import com.vaadin.componentfactory.lookupfield.bean.Person;

import java.util.List;

public class PersonService {
    private PersonData personData = new PersonData();

    public List<Person> fetch(int offset, int limit) {
        int end = offset + limit;
        int size = personData.getPersons().size();
        if (size <= end) {
            end = size;
        }
        return personData.getPersons().subList(offset, end);
    }

    public int count() {
        return personData.getPersons().size();
    }

    public List<Person> fetchAll() {
        return personData.getPersons();
    }
}