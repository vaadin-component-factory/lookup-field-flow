install this skill https://github.com/parttio/dramafinder/tree/master/skills/
vaadin-playwright-test only for the project


reopen claude


Create a new test for a dummy view, the view contains a textfield and a title, test the title and the visibility of the textfield using
the library dramafinder. The test should be in lookup-field-flow/src/test . use Spring boot to create a test server. You can check how
it's done in the dramafinder project pom.xml: https://github.com/parttio/dramafinder/blob/master/pom.xml


It’s not perfect, some issues creating the first test. (and I fixed the dramafinder library to avoid an issue)

Add a GitHub action to validate the PR


/vaadin-playwright-test Create a new view like the @lookup-field-flow-demo/src/main/java/com/vaadin/componentfactory/lookupfield/SimpleView.java to test it with Playwright.
Add the following tests:
- fill the combobox with item1, open the search button "Click to open the search dialog" and check that a dialog is opened. The dialog footer should contain a select and cancel button. The dialog content
  should contain a grid with one row