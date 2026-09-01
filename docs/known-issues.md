# Known application issues

## IOS-CATALOG-001 — Incorrect price sorting

- Application: Sauce Labs My Demo App
- Platform: iOS
- Version: 2.2.2
- Status: Open upstream application defect
- Automation impact: Excluded from the passing cross-platform regression suite

### Steps to reproduce

1. Open the product catalog.
2. Open the sorting menu.
3. Select `Price - Ascending`.
4. Review the product prices from top to bottom.
5. Repeat using `Price - Descending`.

### Expected result

Prices are sorted numerically:

- Ascending: `7.99`, `9.99`, `15.99`, `29.99`, `49.99`
- Descending: `49.99`, `29.99`, `15.99`, `9.99`, `7.99`

### Actual result

Prices are ordered incorrectly. The application compares price values as strings instead of
numeric values.

### Technical notes

The iOS implementation stores prices as strings and compares them directly using `<` and `>`.

Source:
https://github.com/saucelabs/my-demo-app-ios/blob/2.2.2/My%20Demo%20App/Controllers/CatalogViewController.swift

## IOS-A11Y-001 — Product price value is not exposed to accessibility

- Application: Sauce Labs My Demo App
- Platform: iOS
- Version: 2.2.2
- Status: Open upstream application defect

### Actual result

The visible price, for example `$ 29.99`, is exposed to Appium as:

- name: `Product Price`
- label: `Product Price`
- value: `Product Price`

The numerical value cannot therefore be retrieved reliably through the XCUITest accessibility
tree.
