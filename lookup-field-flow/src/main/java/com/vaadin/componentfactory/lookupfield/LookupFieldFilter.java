package com.vaadin.componentfactory.lookupfield;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author jcgueriaud
 */
public interface LookupFieldFilter<FILTERTYPE> {

    Component getComponent();

    void setFilterAction(LookupFieldFilterAction<FILTERTYPE> filterAction);
}
