# Known application issues

## IOS-CATALOG-001 — Incorrect price sorting

- Application: Sauce Labs My Demo App
- Platform: iOS
- Version: 2.2.2
- Status: Open upstream application defect
- Automation impact: iOS price-sorting assertions are excluded from the cross-platform regression
  suite

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
[CatalogViewController.swift](https://github.com/saucelabs/my-demo-app-ios/blob/2.2.2/My%20Demo%20App/Controllers/CatalogViewController.swift)

## IOS-A11Y-001 — Product price value is not exposed to accessibility

- Application: Sauce Labs My Demo App
- Platform: iOS
- Version: 2.2.2
- Status: Open upstream application defect
- Automation impact: Prevents reliable price extraction through the XCUITest accessibility tree

### Actual result

The visible price, for example `$ 29.99`, is exposed to Appium as:

- name: `Product Price`
- label: `Product Price`
- value: `Product Price`

The numerical value cannot therefore be retrieved reliably through the XCUITest accessibility
tree.

## IOS-WEBVIEW-001 — WebView context is not exposed to Appium

- Application: Sauce Labs My Demo App
- Platform: iOS
- Version: 2.2.2
- Confirmed target: iOS Simulator
- Status: Upstream automation limitation
- Automation impact: DOM-level WebView validation is intentionally skipped on iOS

### Expected result

After opening a URL from the Webview screen, Appium exposes an additional `WEBVIEW_*` context,
allowing the embedded page DOM to be automated.

### Actual result

The website is displayed inside the application, but Appium exposes only the `NATIVE_APP`
context. Appium Inspector reports that no additional contexts have been detected.

### Technical notes

The version 2.2.2 implementation loads the requested URL into a `WKWebView`, but does not enable
its `isInspectable` property. The upstream application would need to expose the WebView for
inspection on supported iOS versions.

Source:
[WebViewHandlerViewController.swift](https://github.com/saucelabs/my-demo-app-ios/blob/2.2.2/My%20Demo%20App/Controllers/WebViewHandlerViewController.swift)
