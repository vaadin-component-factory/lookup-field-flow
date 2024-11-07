# 

## Development instructions

Build the project and install the add-on locally:
```
mvn clean install
```
Starting the demo server:

Go to lookup-field-flow-demo and run:
```
mvn jetty:run
```

This deploys demo at http://localhost:8080

## Description 

The Lookup field component allows you to search a specific record with:
* a combobox
* a dialog and a grid that can contains all the  fields

## Compatibility

- Version 1.x.x -> Up to Vaadin 17.x.x
- Version 2.x.x -> Vaadin 17+
- Version 3.x.x -> Vaadin 23.x
- Version 4.x.x -> Vaadin 24.x
- Version 5.x.x -> Vaadin 24.x.x (improved accessibility)

## How to use it

Create a new component LookupField and use it like a Field.
The API is quite similar to a Vaadin Combobox.


## Examples

### Basic example with String

```
    public SimpleView() {
        LookupField<String> lookupField = new LookupField<>();
        List<String> items = Arrays.asList("item1","item2", "item3");
        lookupField.setDataProvider(DataProvider.ofCollection(items));
        lookupField.getGrid().addColumn(s -> s).setHeader("item");
        lookupField.setLabel("Item selector");
        add(lookupField);
    }
```

### Basic example with an Object

```
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
```

### Internationalization 

You can change the captions of the buttons, dialog header, serach label.

```
    public I18nView() {
        LookupField<String> lookupField = new LookupField<>();
        List<String> items = Arrays.asList("item1","item2", "item3");
        lookupField.setDataProvider(DataProvider.ofCollection(items));
        lookupField.getGrid().addColumn(s -> s).setHeader("item");
        // set the label of the field
        lookupField.setLabel("Item selector");
        //set the header text of the dialog
        lookupField.setHeader("utilisateurs");
        // translate the cpations of the component
        lookupField.setI18n(new LookupField.LookupFieldI18n()
            .setSearcharialabel("Cliquer pour ouvrir la fenêtre de recherche")
            .setSelect("Sélectionner")
            .setHeaderprefix("Recherche:")
            .setSearch("Recherche")
            .setHeaderpostfix("")
            .setCancel("Annuler"));
        add(lookupField);
    }
```

## Demo

You can check the demo here: https://componentfactory.app.fi/lookup-field-flow-demo/

## Missing features or bugs

You can report any issue or missing feature on github: https://github.com/vaadin-component-factory/lookup-field-flow