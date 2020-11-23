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
        final VerticalLayout menuLayout = new VerticalLayout(simple, personLookupField, personLabelLookupField);
        addToDrawer(menuLayout);
        addToNavbar(drawerToggle);
    }

}