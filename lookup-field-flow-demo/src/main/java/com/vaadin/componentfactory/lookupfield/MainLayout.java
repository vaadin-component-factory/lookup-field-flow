package com.vaadin.componentfactory.lookupfield;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {

    public MainLayout() {
        final DrawerToggle drawerToggle = new DrawerToggle();
        final RouterLink simple = new RouterLink("String Lookup Field", SimpleView.class);
        final RouterLink personLookupField = new RouterLink("Person Lookup Field", PersonView.class);
        final RouterLink personLabelLookupField = new RouterLink("Person with label", PersonLabelGeneratorView.class);
        final RouterLink i18nView = new RouterLink("I18n example", I18nView.class);
        final RouterLink binderView = new RouterLink("Binder example", BinderView.class);
        final RouterLink customHeader = new RouterLink("Custom Header", CustomHeaderView.class);
        final VerticalLayout menuLayout = new VerticalLayout(personLookupField, simple, personLabelLookupField, i18nView, binderView, customHeader);
        addToDrawer(menuLayout);
        addToNavbar(drawerToggle);
    }

}