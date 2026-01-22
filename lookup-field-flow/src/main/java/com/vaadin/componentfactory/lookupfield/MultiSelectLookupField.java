package com.vaadin.componentfactory.lookupfield;

/*
 * #%L
 * lookup-field-flow
 * %%
 * Copyright (C) 2020 - 2026 Vaadin Ltd
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.function.SerializableFunction;


/**
 * Server-side component for the {@code vcf-lookup-field} webcomponent.
 *
 * The LookupField is a combination of a combobox and a dialog for advanced search.
 *
 *
 * @param <T> the type of the items to be inserted in the combo box
 */

public class MultiSelectLookupField<T> extends CustomFilterMultiSelectLookupField<T, String> {

    public MultiSelectLookupField() {
        this(new Grid<>(), new MultiSelectComboBox<>());
    }

    public MultiSelectLookupField(Class<T> beanType) {
        this(new Grid<>(beanType), new MultiSelectComboBox<>());
    }

    public MultiSelectLookupField(Grid<T> grid, MultiSelectComboBox<T> comboBox) {
        super(grid, comboBox, SerializableFunction.identity(), SerializableFunction.identity());
    }

}
