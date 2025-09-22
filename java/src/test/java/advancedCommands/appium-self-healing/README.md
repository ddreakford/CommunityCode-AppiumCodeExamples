# Digital.ai Continuous Testing - AI-Powered Self Healing for Appium

The AI-Powered Self Healing uses AI techniques to understand and repair element mismatches autonomously without human intervention.

## Prerequisites

- Only works on SaaS Hosted Environments
- If you are a SaaS based customer and Self Healing is not enabled, reach out to support@digital.ai to have it enabled

## Limitations

- Self-Healing does not work with Native iOS Tests. It works with iOS Web, Android Native & Web.
- Support for Native iOS Tests coming soon...

## Test Setup

Configure your environment variables in the `.env` file:

```bash
CLOUD_URL=https://your-cloud-url.experitest.com
ACCESS_KEY=your_access_key_here
ANDROID_DEVICE_QUERY="@os='android' and contains(@name, 'Galaxy S2')"
APPIUM_VERSION=2.1.3

# Self-healing specific configuration
UNMODIFIED_BUILD_URL=https://ct-demo-content.s3.us-west-2.amazonaws.com/com.experitest.ExperiBank_.LoginActivity_ver_UNMODIFIED.apk
MODIFIED_BUILD_URL=https://ct-demo-content.s3.us-west-2.amazonaws.com/com.experitest.ExperiBank_.LoginActivity_ver_MODIFIED.apk
```

[Obtain your Access Key](https://docs.digital.ai/bundle/TE/page/obtaining_access_key.html)

## Description of Test Logic

For demonstration purposes, we will be using an Android APK Application, developed by Digital.ai.

Two application builds will be used:
- One build that is **UNMODIFIED** with original element locators
- One build that is **MODIFIED** where the username Text Field locator has changed

The Automated Script performs the following steps:
- Enter "company" into Username Text Field
- Enter "company" into Password Text Field
- Click on the Login Button
- Verify user landed on Dashboard

## Test Classes

### AppiumSelfHealingRegularAndroidTest
This class has Self Healing **disabled** for demonstration purposes. The test will:
- Pass against the unmodified build
- Fail against the modified build

### AppiumSelfHealingAndroidTest
This class has Self Healing **enabled**. The test will:
- Pass against the unmodified build
- Pass against the modified build (self-healing repairs the element mismatch)

## Running the Tests
*Note:* [The "containerized" option](../../../../../../architecture/CONTAINERIZED_TESTING_GUIDE.md) can be used to run these tests in lieu of natively installing the necessary dependencies.

### When Self-Healing is disabled

With Self-Healing disabled, the first test will pass against the unmodified build. The second test will always fail against the modified build.

To try this out, run the following command from your Terminal window:

```bash
./gradlew test -Psuites=src/test/resources/appium-self-healing/regular_tests.xml
```

### When Self-Healing is enabled

With Self-Healing enabled, the first test will pass against the unmodified build. The second test will also pass, even though it is running against a modified build.

The Self-Healing kicks in as it will detect that the Username Text Field locator has changed, automatically finding the most appropriate replacement, and continue running the test.

To try this out, run the following command from your Terminal window:

```bash
./gradlew test -Psuites=src/test/resources/appium-self-healing/self_healing_tests.xml
```



### Run both test suites

To run both regular and self-healing tests:

```bash
./gradlew test -Psuites=src/test/resources/appium-self-healing/regular_tests.xml,src/test/resources/appium-self-healing/self_healing_tests.xml
```

## Viewing the Test Results

Navigate to your Digital.ai Testing Reporter page:

```
https://<your_cloud_url>/reporter/reporter/tests
```

Under "Manage Columns", search for the word Heal, and you should see two attributes, "Healed Locators" and "selfHealing":

Select both and click on Save.

Once saved, the column will now reflect tests that ran with Self-Healing and if any tests Healed.

You can also open up the individual Test Reports to find the specific details about what was Healed.

## Project Structure

```
appium-self-healing/
├── helpers/
│   └── SelfHealingHelpers.java          # Helper for app upload and configuration
├── tests/
│   ├── AppiumSelfHealingRegularAndroidTest.java  # Tests without self-healing
│   └── AppiumSelfHealingAndroidTest.java          # Tests with self-healing enabled
└── README.md                             # This documentation

src/test/resources/appium-self-healing/
├── regular_tests.xml                     # TestNG suite for regular tests
└── self_healing_tests.xml               # TestNG suite for self-healing tests
```

## Key Differences from Original

This modernized version includes:

- **Appium 9.4.0**: Updated from 8.6.0 with modern UiAutomator2Options
- **Environment Configuration**: Uses project's `.env` file instead of properties file
- **Modern Dependencies**: Kong Unirest instead of Mashape Unirest
- **Gradle Build**: Integrated with project's Gradle build system
- **Enhanced Error Handling**: Improved exception management and logging

## References

- [Docs - Digital.ai Self Healing Documentation](https://docs.digital.ai/bundle/TE/page/appium_self-healing.html)
- [YouTube - Digital.ai Self Healing Demo](https://www.youtube.com/watch?v=pfSQm1NHj4s)