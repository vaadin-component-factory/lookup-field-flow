package com.vaadin.componentfactory.lookupfield;

/**
 * Filter action.
 *
 * @param <T> the type of the filter value passed to {@link #filter(Object)}
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