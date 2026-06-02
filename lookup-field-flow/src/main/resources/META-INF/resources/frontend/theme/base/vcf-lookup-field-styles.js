import { registerStyles, css } from '@vaadin/vaadin-themable-mixin/register-styles.js';

registerStyles(
  'vaadin-dialog-overlay',
  css`
    :host([theme~="lookup-dialog"]) [part="overlay"] {
        min-width: var(--vcf-lookup-field-dialog-min-width, 400px);
    }   
  ` 
);

registerStyles(
  'vaadin-combo-box',
  css`
    :host([theme~='full-width']) [part="input-field"] {
        margin-inline-end: var(--search-button-width);
    }
    :host([theme~='integrated']) [part="input-field"] {
        border-top-right-radius: 0;
        border-bottom-right-radius: 0;
    }
       
  ` 
);
