This folder is a copy of https://github.com/vaadin-component-factory/vcf-lookup-field to make the development easier.

You can switch from the npm package to this folder by updating the AbstractLookupField annotations:
```
//@JsModule("@vaadin-component-factory/vcf-lookup-field")
@JsModule("./src/vcf-lookup-field.js")
```

Once it's tested you can update the vcf component and publish it. Then update the version in @NpmPackage