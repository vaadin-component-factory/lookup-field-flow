import { html, PolymerElement } from '@polymer/polymer/polymer-element';
import { ThemableMixin } from '@vaadin/vaadin-themable-mixin';
import { ElementMixin } from '@vaadin/vaadin-element-mixin';
import { FlattenedNodesObserver } from '@polymer/polymer/lib/utils/flattened-nodes-observer.js';
import '@vaadin/vaadin-button';
import '@vaadin/vaadin-combo-box';
import '@vaadin/vaadin-text-field';
import '@vaadin-component-factory/vcf-enhanced-dialog';
import '@vaadin/vaadin-grid';
import '@vaadin/vaadin-grid/vaadin-grid-filter';
import '@polymer/iron-icon';
import '@vaadin/vaadin-icons';
/**
 * `<vcf-lookup-field>` [element-description]
 *
 * ```html
 * <vcf-lookup-field></vcf-lookup-field>
 * ```
 *
 * ### Styling
 *
 * The following custom properties are available for styling:
 *
 * Custom property | Description | Default
 * ----------------|-------------|-------------
 *
 * The following shadow DOM parts are available for styling:
 *
 * Part name | Description
 * ----------------|----------------
 * `part` | Example part
 *
 * The following state attributes are available for styling:
 *
 * Attribute    | Description | Part name
 * -------------|-------------|------------
 * `attribute` | Example styling attribute | :host
 *
 * @memberof Vaadin
 * @mixes ElementMixin
 * @mixes ThemableMixin
 * @demo demo/index.html
 */

class VcfLookupField extends ElementMixin(ThemableMixin(PolymerElement)) {
    static get template() {
        return html`
      <style>
        :host {
          display: inline-block;
        }
        .container {
          width: 100%;
          display: inline-flex;
          align-items: baseline;
        }
      </style>
      <div class="container">
        <slot name="field" id="fieldSlot">
          <vaadin-combo-box
            clear-button-visible
            allow-custom-value
            items="{{items}}"
            item-label-path="{{itemLabelPath}}"
            item-value-path="{{itemValuePath}}"
            required$="[[required]]"
            readonly$="[[readonly]]"
          ></vaadin-combo-box>
        </slot>

        <vaadin-button class="search-button" theme="icon" on-click="__open" aria-label="[[i18n.searcharialabel]]" disabled$="[[readonly]]">
          <iron-icon icon="vaadin:search"></iron-icon>
        </vaadin-button>
        <vcf-enhanced-dialog aria-label="lookup-grid" id="dialog" theme$="[[theme]]" modeless$="[[modeless]]" draggable$="[[draggable]]" resizable$="[[resizable]]">
              <header id="dialogheader" slot="header" class="draggable">[[i18n.titleprefix]] {{title}} [[i18n.titlepostfix]]</header>
              <footer id="dialogfooter" slot="footer">
                <vaadin-button
                  id="selectbtn"
                  theme="primary"
                  disabled
                  aria-disabled="true"
                  role="button"
                  on-click="__select"
                >
                [[i18n.select]]
                </vaadin-button>
                <vaadin-button tabindex="0" role="button" on-click="__close" theme="tertiary">
                  [[i18n.cancel]]
                </vaadin-button>
              </footer>
                <div id="dialogmain">
                    <vaadin-text-field
                      class="lookup-field-filter"
                      style="width:100%;"
                      tabindex="0"
                      label="[[i18n.search]]"
                      on-value-changed="__filterGrid"
                      clear-button-visible
                      value= "{{_filterdata}}"
                      >
                        <iron-icon icon="lumo:search" slot="suffix"></iron-icon>
                    </vaadin-text-field>
                </div>
          </vcf-enhanced-dialog>
          <slot name="grid" style="display:none;" id="gridSlot">
              <vaadin-grid items="[[filterItems(items, _filterdata)]]">
                <vaadin-grid-column path="name" path="{{itemLabelPath}}"></vaadin-grid-column>
              </vaadin-grid>
          </slot>
      </div>
    `;
    }

    constructor() {
        super();
        this._observer = new FlattenedNodesObserver(this, info => {
            this.__onDomChange(info.addedNodes);
        });
    }

    ready() {
        super.ready();
        this._grid = this.$.gridSlot.firstElementChild;
        this._field = this.$.fieldSlot.firstElementChild;
        const that = this;

        if (this._grid) {
            this._grid.addEventListener('active-item-changed', e => {
                that.__onSelectItem(e);
            });
        }

        if (this._field) {
            this._field.addEventListener('filter-changed', function(e) {
                that._filterValue = e.detail.value;
            });
        }

        /**
         * fill the dialog content because template in template is not working well
         */
        this.$.dialog.renderer = function(root, dialog) {
            if (root.firstElementChild) {
                return;
            }
            if (that.title) {
                root.appendChild(that.$.dialogheader);
            }
            root.appendChild(that.$.dialogmain);
            that.$.dialogmain.appendChild(that._grid);
            root.appendChild(that.$.dialogfooter);
        }
    }
    __opendialog() {
        this.$.basic.opened = true;
    }
    __onSelectItem(event) {
        const item = event.detail.value;
        if (item) {
            this.$.selectbtn.removeAttribute('disabled');
        } else {
            this.$.selectbtn.setAttribute('disabled', 'disabled');
        }
        this._grid.selectedItems = item ? [item] : [];
        this._gridSelectedItem = item;
    }

    /** @private */
    __onDomChange(nodes) {
        const that = this;
        nodes.forEach(node => {
            if (node.getAttribute) {
                if (node.getAttribute('slot') == 'grid') {
                    this._grid = node;
                    this._grid.addEventListener('active-item-changed', e => {
                        that.__onSelectItem(e);
                    });
                } else if (node.getAttribute('slot') == 'field') {
                    this._field = node;
                    this._field.style.flexGrow = 1;
                    this._field.addEventListener('filter-changed', function(e) {
                        that._filterValue = e.detail.value;
                    });
                }
            }
        });
    }

    __filterGrid(event) {
        if (this.$server) {
            this.$server.filterGrid(event.detail.value);
        }
    }

    filterItems(items, filterData) {
        if (items && filterData) {
            return items.filter(item => this._getItemLabel(item).toLowerCase().includes(filterData.toLowerCase()));
        } else {
            return items;
        }
    }

    /** @private */
    __open() {
        this.$.dialog.opened = true;
        this._filterdata = this._field.inputElement.value;
        if (this.$server) {
            this.$server.copyFieldValueToGrid();
            this.$server.filterGrid(this._filterdata);
        } else {
            const item = this._field.selectedItem;
            this._grid.selectedItems = item ? [item] : [];
            this._gridSelectedItem = item;
            if (item) {
                this.$.selectbtn.removeAttribute('disabled');
            } else {
                this.$.selectbtn.setAttribute('disabled', 'disabled');
            }
        }
    }
    /** @private */
    __close() {
        this.$.dialog.opened = false;
    }
    /** @private */
    __select() {
        if (this.$server) {
            this.$server.copyFieldValueFromGrid();
        } else {
            this._field.selectedItem = this._gridSelectedItem;
        }

        this.$.dialog.opened = false;
    }

    _getItemLabel(item) {
        let label = item && this.itemLabelPath ? this.get(this.itemLabelPath, item) : undefined;
        if (label === undefined) {
            label = item ? item.toString() : '';
        }
        return label;
    }

    _itemsChanged() {
        if (this._field) {
            this._field.items = this.items;
        }
    }

    static get is() {
        return 'vcf-lookup-field';
    }

    static get version() {
        return '1.0.0';
    }

    static get properties() {
        return {
            title: {
                type: String
            },
            /**
             * A full set of items to filter the visible options from.
             * The items can be of either `String` or `Object` type.
             * @type {!Array<string> | undefined}
             */
            items: {
                type: Array,
                observer: '_itemsChanged'
            },

            _filterdata: {
                type: String
            },
            /**
             * Path for label of the item. If `items` is an array of objects, the
             * `itemLabelPath` is used to fetch the displayed string label for each
             * item.
             *
             * The item label is also used for matching items when processing user
             * input, i.e., for filtering and selecting items.
             *
             * When using item templates, the property is still needed because it is used
             * for filtering, and for displaying the selected item value in the input box.
             * @attr {string} item-label-path
             * @type {string}
             */
            itemLabelPath: {
                type: String,
                value: 'label'
            },

            /**
             * Path for the value of the item. If `items` is an array of objects, the
             * `itemValuePath:` is used to fetch the string value for the selected
             * item.
             *
             * The item value is used in the `value` property of the combo box,
             * to provide the form value.
             * @attr {string} item-value-path
             * @type {string}
             */
            itemValuePath: {
                type: String,
                value: 'value'
            },

            /**
             * @type {Boolean}
             */
            modeless: Boolean,
            /**
             * @type {Boolean}
             */
            draggable: Boolean,
            /**
             * @type {Boolean}
             */
            resizable: Boolean,

            /**
             * @type {Boolean}
             */
            required: Boolean,

            /**
             * @type {Boolean}
             */
            readonly: Boolean,

            /**
             * @type {String}
             */
            theme: {
                type: String,
                value: 'lookup-dialog'
            },

            /**
             * The object used to localize this component.
             * For changing the default localization, change the entire
             * _i18n_ object or just the property you want to modify.
             **/

            i18n: {
                type: Object,
                value: function() {
                    return {
                        select: 'Select',
                        cancel: 'Cancel',
                        search: 'Search',
                        searcharialabel: 'Click to open the search dialog',
                        titleprefix: 'Open',
                        titlepostfix: 'dialog'
                    };
                }
            },
        };
    }
}

customElements.define(VcfLookupField.is, VcfLookupField);

/**
 * @namespace Vaadin
 */
window.Vaadin.VcfLookupField = VcfLookupField;
