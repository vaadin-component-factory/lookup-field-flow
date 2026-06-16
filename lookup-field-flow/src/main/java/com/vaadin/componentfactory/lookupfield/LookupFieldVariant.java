package com.vaadin.componentfactory.lookupfield;

/**
 * Theme variants that can be applied to the lookup field.
 */
public enum LookupFieldVariant {
    /** Small size variant. */
    SIZE_SMALL("small"),
    /** Medium size variant. */
    SIZE_MEDIUM("medium"),
    /** Large size variant. */
    SIZE_LARGE("large"),
    /** Variant that makes the label take the full width. */
    FULL_WIDTH_LABEL("full-width"),
    /** Variant that renders the field in an integrated style. */
    INTEGRATED("integrated");

    private final String variant;

    LookupFieldVariant(String variant) {
        this.variant = variant;
    }

    /**
     * Gets the theme name used by this variant.
     *
     * @return the variant theme name
     */
    public String getVariantName() {
        return variant;
    }
}
