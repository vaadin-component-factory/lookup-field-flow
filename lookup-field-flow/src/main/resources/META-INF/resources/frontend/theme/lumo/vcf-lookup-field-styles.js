import '@vaadin/vaadin-lumo-styles/color.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import { html } from '@polymer/polymer/lib/utils/html-tag.js';

const $_documentContainer = html`<dom-module id="dialog-layout-overlay-theme" theme-for="vaadin-dialog-overlay">
  <template>
    <style>
      :host([theme~="lookup-dialog"]) [part="overlay"],
      :host([theme~="lookup-dialog"]) [part="content"] {
        padding: 0;
        max-height: 100vh;
      }

      :host([theme~="lookup-dialog"]) header {
        font-size: var(--lumo-font-size-xl);
        margin: 5px;
      }
      
      :host([theme~="lookup-dialog"]) main {
        margin: 5px;
      }
      :host([theme~="lookup-dialog"]) footer {
        paading: 5px;
        background-color: var(--_lumo-button-background-color, var(--lumo-contrast-5pct));
      }
    </style>
  </template>
</dom-module>
    <style>
      .lookup-field-dialog-content header {
        font-size: var(--lumo-font-size-xl);
        margin: var(--lumo-space-l);
        margin-bottom: 0;
        margin-top: var(--lumo-space-l);
      }
      
      .lookup-field-dialog-content main {
        margin: var(--lumo-space-l);
        margin-top: 0;
        margin-bottom: var(--lumo-space-s);
      }
      .lookup-field-dialog-content footer {
        padding-left: var(--lumo-space-l);
        padding-right: var(--lumo-space-l);
        padding-top: var(--lumo-space-xs);
        padding-bottom: var(--lumo-space-xs);
        background-color: var(--_lumo-button-background-color, var(--lumo-contrast-5pct));
      }
      .lookup-field-dialog-content .lookup-field-filter {
        margin-bottom: var(--lumo-space-xs);
      }
    </style>`;

document.head.appendChild($_documentContainer.content);
