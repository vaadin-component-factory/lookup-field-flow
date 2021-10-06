package com.vaadin.componentfactory.lookupfield.bean;

/**
 * @author jcgueriaud
 */
public class PersonFilter {

    private String firstName;
    private String lastName;

    public PersonFilter(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public PersonFilter setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public PersonFilter setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
