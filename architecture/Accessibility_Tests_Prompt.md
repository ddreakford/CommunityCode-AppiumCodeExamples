# Accessibility Scan Tests Creation Prompt
This is the initial prompt used to run accessibility scans via the integration with [Deque axe Devtools for Mobile&copy;](https://docs.deque.com/devtools-mobile/2025.7.2/en/appium-scan-and-handle)

Generate two tests that run an accessibility scan using Deque axe Devtools for Mobile: one for IOS and one for Android. Review the requirements listed below, create a plan to accomplish, and write the plan to a file, `ACCESSIBILITY_TESTS_PLAN.md`. Check with me for confirmation before executing the plan.

- Use additional environment settings to specify some of the behavior of this integration. Use this project's convention for environment settings. Add an environment variable, `AXE_DEVTOOLS_API_KEY` to the `.env.example` file. I will copy the name of this variable to the `.env` file and add my API key. Make sure to include this step in the instructions for setting up this integration so that these accessibility tests can be run.
- The new tests should be located in  @java/src/test/java/advancedCommands/ and they should be part of the package `advancedCommands.accessibility-scan`.
- For the IOS test, use @java/src/test/java/quickStartTests/IOSQuickStartTest.java as a template. Name the IOS test "IOSAccessibilityTest". Include a single test method, "accessibilityScan". Have the method call the Appium IOS driver function `execute_script("mobile: axeScan", settings)`. 
- For the Android test, use @java/src/test/java/quickStartTests/AndroidQuickStartTest.java as a template. Name the Android test "AndroidAccessibilityTest". Include a single test method, "accessibilityScan".  Have the method call the Appium Android driver function `execute_script("mobile: axeScan", settings)`.
- Before `execute_script` is called by these tests, create the `settings` object with an `apiKey` key, and a `scanName` key. The value of `apiKey` should be taken from the environment variable, `AXE_DEVTOOLS_API_KEY`.
- Write the success or failure status from the call to `execute_script` using common convention. If there is an error, handle the error and write a descriptive message.
- Create a utility that is used by both the IOS and Android tests to write the results to a file in the `reports` directory used per the convention of this project. Here is an [example](https://docs.deque.com/devtools-mobile/2025.7.2/en/appium-save-results) of how to write the results to a JSON file. Save the results to both a JSON and an HTML file.



