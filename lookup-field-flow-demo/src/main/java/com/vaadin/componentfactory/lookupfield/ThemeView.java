package com.vaadin.componentfactory.lookupfield;

import java.util.Arrays;
import java.util.List;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

/**
 * Theme examples
 */
@Route(value = "theme", layout = MainLayout.class)
public class ThemeView extends VerticalLayout {

    public ThemeView() {
        add(
                buildLookupField("Small", LookupFieldVariant.SIZE_SMALL),
                buildLookupField("Medium", LookupFieldVariant.SIZE_MEDIUM),
                buildLookupField("Large", LookupFieldVariant.SIZE_LARGE),
                buildLookupField("Integrated", LookupFieldVariant.INTEGRATED),
                buildLookupField("Normal theme with a long label to test"),
                buildLookupField("Full width label theme with a long label to test", LookupFieldVariant.FULL_WIDTH_LABEL),
                buildLookupField("Full width label theme with a long label to test", LookupFieldVariant.FULL_WIDTH_LABEL, LookupFieldVariant.INTEGRATED),
                buildMultiSelectLookupField("Normal theme with a long label to test"),
                buildMultiSelectLookupField("Full width label theme with a long label to test", LookupFieldVariant.FULL_WIDTH_LABEL),
                buildMultiSelectLookupField("Full width label theme with a long label to test", LookupFieldVariant.FULL_WIDTH_LABEL, LookupFieldVariant.INTEGRATED)
        );
    }

    private static LookupField<String> buildLookupField(String label, LookupFieldVariant... variants) {
        LookupField<String> lookupField = new LookupField<>();
        List<String> items = Arrays.asList("item1", "item2", "item3");
        lookupField.setDataProvider(DataProvider.ofCollection(items));
        lookupField.getGrid().addColumn(s -> s).setHeader("item");
        lookupField.setLabel(label);
        lookupField.addThemeVariants(variants);
        return lookupField;
    }

    private static MultiSelectLookupField<String> buildMultiSelectLookupField(String label, LookupFieldVariant... variants) {
        MultiSelectLookupField<String> lookupField = new MultiSelectLookupField<>();
        List<String> items = Arrays.asList("item1", "item2", "item3");
        lookupField.setDataProvider(DataProvider.ofCollection(items));
        lookupField.getGrid().addColumn(s -> s).setHeader("item");
        lookupField.setLabel(label);
        lookupField.addThemeVariants(variants);
        return lookupField;
    }

}
