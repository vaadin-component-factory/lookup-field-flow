package com.vaadin.componentfactory.lookupfield;

/**
 * @author jcgueriaud
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