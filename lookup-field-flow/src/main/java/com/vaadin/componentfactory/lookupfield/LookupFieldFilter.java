package com.vaadin.componentfactory.lookupfield;

import com.vaadin.flow.component.Component;

/**
 * Class for the custom filter component
 * The component need to call manually the filter action. For example
 * filterButton.addClickListener(e -> {
 *    if (fieldFilterAction != null) {
 *       fieldFilterAction.filter(new FILTERTYPE);
 *    }
 * });
 */
public interface LookupFieldFilter<FILTERTYPE> {

    /**
     *
     * @return Filter component
     */
    Component getComponent();

    /**
     *
     * @param filterAction action to call when you want to filter the grid
     */
    void setFilterAction(LookupFieldFilterAction<FILTERTYPE> filterAction);
}
