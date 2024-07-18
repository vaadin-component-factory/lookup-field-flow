package com.vaadin.componentfactory.lookupfield;

public enum LookupFieldVariant {
    SIZE_SMALL("small"), SIZE_MEDIUM("medium"), SIZE_LARGE("large"), FULL_WIDTH_LABEL("full-width"), INTEGRATED("integrated");

    private final String variant;

    LookupFieldVariant(String variant) {
        this.variant = variant;
    }

    public String getVariantName() {
        return variant;
    }
}
