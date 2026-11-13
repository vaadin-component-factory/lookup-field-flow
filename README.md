# Lookup Field Flow

## Description 

The Lookup field component allows you to search a specific record with:
* a combobox
* a dialog and a grid that can contains all the  fields

## Important information about versioning
**Component versions 23.x and 24.x were deprecated in order to follow Semanting Versioning practices. Please use latest version 3.x for Vaadin 23 and version 4/5.x for Vaadin 24.**  

## Compatibility

- Version 1.x.x -> Up to Vaadin 17.x.x
- Version 2.x.x -> Vaadin 17+
- Version 3.x.x -> Vaadin 23.x
- Version 4.x.x -> Vaadin 24.x
- Version 5.x.x -> Vaadin 24.x.x (improved accessibility)
- Version 6.x.x -> Vaadin 25.x
- Version 7.x.x -> Vaadin 25.1+
- Version 8.x.x -> Vaadin 25.1+ (web component based on Lit)

Since version 8.0.0, the add-on uses `vcf-lookup-field` 8.x, which is built with Lit instead of Polymer.
The dialog is now part of the lookup field's shadow root, so styles that targeted `vaadin-dialog`
must use `vcf-lookup-field::part(dialog-overlay)`. See the
[web component migration notes](https://github.com/vaadin-component-factory/vcf-lookup-field#migrating-to-800)
for the other breaking changes.

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
