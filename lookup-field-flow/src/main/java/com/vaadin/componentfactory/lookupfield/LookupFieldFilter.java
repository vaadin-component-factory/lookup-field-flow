package com.vaadin.componentfactory.lookupfield;

import com.vaadin.flow.component.Component;

/**
 * Class for the custom filter component
 * The component need to call manually the filter action. For example
 * <pre>
 * filterButton.addClickListener(e -&#62; {
 *    if (fieldFilterAction != null) {
 *       fieldFilterAction.filter(new FILTERTYPE);
 *    }
 * });
 * </pre>
 *
 * @param <FILTERTYPE> the type of the filter value produced by this component
 */
public interface LookupFieldFilter<FILTERTYPE> {

    /**
     * Returns the component used to render the filter.
     *
     * @return Filter component
     */
    Component getComponent();

    /**
     * Sets the action that the filter component calls to filter the grid.
     *
     * @param filterAction action to call when you want to filter the grid
     */
    void setFilterAction(LookupFieldFilterAction<FILTERTYPE> filterAction);
}
