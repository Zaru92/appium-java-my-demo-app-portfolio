## Summary

<!-- Describe what this pull request changes and why. -->

*

## Changes

<!-- List the most important implementation or documentation changes. -->

*

## Verification

<!-- Select every environment that was actually verified. -->

* [ ] Framework verification
  (`./mvnw --batch-mode --no-transfer-progress -Dsurefire.suiteXmlFiles=src/test/resources/suites/testng-framework.xml clean verify`)
* [ ] Android emulator
* [ ] Android real device
* [ ] iOS simulator
* [ ] iOS real device
* [ ] Documentation-only change

## Evidence

<!-- Link a GitHub Actions run or attach relevant logs, screenshots, or Allure artifacts. Use "Not applicable" when evidence is unnecessary. -->

## Checklist

* [ ] The change is limited to the stated scope.
* [ ] Tests were added or updated when behavior changed, or this is not applicable.
* [ ] Android and iOS differences were handled or documented, when applicable.
* [ ] Documentation was updated when commands, configuration, or behavior changed.
* [ ] No secrets, device UDIDs, local application binaries, or machine-specific paths were committed.
