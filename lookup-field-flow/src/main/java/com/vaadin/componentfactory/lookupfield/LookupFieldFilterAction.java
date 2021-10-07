package com.vaadin.componentfactory.lookupfield;

/**
 * Filter action
 */
@FunctionalInterface
public interface LookupFieldFilterAction<T> {
    /**
     * Apply the filter
     *
     * @param t the input argument
     */
    void filter(T t);
}