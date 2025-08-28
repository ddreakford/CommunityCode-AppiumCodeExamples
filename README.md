# **Appium Open Source - Code Examples**
This project demonstrates how to use Appium for native applications with both Java/TestNG and Python/pytest implementations.

## **Project Structure**

```
CommunityCode-AppiumCodeExamples/
├── java/                    # Java/TestNG tests
│   ├── build.gradle        # Java build configuration
│   ├── testng.xml         # TestNG suite configuration
│   └── src/test/          # Java test sources
├── python/                 # Python/pytest tests
│   ├── pyproject.toml     # Python project & dependencies
│   ├── conftest.py        # Pytest configuration & fixtures
│   └── tests/             # Python test sources
└── shared/                 # Shared test resources
```

## **Quick Start**

### Java/TestNG Tests
```bash
# Run Java tests
./gradlew javaTest

# Or from java directory
cd java && ./gradlew test
```

### Python/pytest Tests
```bash
# Install dependencies and run Python tests
./gradlew pythonTest

# Or manually with uv
cd python
uv sync
uv run pytest
```

### Run All Tests
```bash
./gradlew test
```

## **Container Testing**

For production-ready, isolated test execution, use the containerized testing environment:

```bash
# Quick start with Docker Compose
docker-compose up --build

# Run specific test suites
docker-compose run --rm appium-tests --java --tests=quickStart
docker-compose run --rm appium-tests --python --platform=android
docker-compose run --rm appium-tests --all --parallel=6
```

📋 **Complete container documentation**: See [CONTAINER_TESTING.md](CONTAINER_TESTING.md)

## **Requirements**

### Local Development
- **Java**: JDK 11+, Gradle
- **Python**: Python 3.9+, uv package manager
- **Appium**: Appium server running on localhost:4723

### Container Testing (Recommended)
- **Docker**: Docker Engine 20.10+
- **Docker Compose**: 2.0+ (optional)
- **Digital.ai Testing Cloud**: Access credentials

## **Notes**
* The tests run on two applications: Eribank and UIcatalog. Therefore, make sure that you have these apps\
in your cloud.
* Different versions of different applications can have differences in the xpath.  If one test fails on xpath,\
 change it to the correct one.


## **Android and iOS Testing**
Most commands and capabilities have two implementations: one for Android and the other for IOS.


## **Project structure**
### Package & Classes Details

#### quickStartTests
This package includes basic tests for Android and ios. Some basic Appium commands are present in this package such as click(), findElement(), and sendKeys().


#### optionalCapabilities
This package uses optional capabilities that can be used to run the Appium test. Aside from the mandatory capabilities, you can set the following optional capabilities:
- buildVersion: Install/launch the app by build version.
- commandScreenshot: In video report, take a screenshot before and after every action when commandScreenshot is set to true.
- doNotGoHomeOnQuit: Device remains in the last state even after ending the test when dontGoHomeOnQuit is set to true.
- installOnlyForUpdate: Install the app only if an older version is already installed on the device.
- releaseDevice: Gives the capability to not release a device after performing driver.quit().
- releaseVersion: Install/launch the app by release version.
- reportDisable: Do not generate a report.


#### advancedCommands
These are special commands using the executeScript command. 
  - adbPushFile: Perform the ADB push file to device command.
  - adbPullFile: Perform the ADB pull file from device command.
  - audioPlay: Play an audio file.
  - audioRecording: Record to a file, from the device.
  - installApp: Install the application with given name/unique name/build version/release version/both build and release version.
  - startLoggingDevice: Start the device log. The log is written to the path provided by the user.
  - stopLoggingDevice: Stop the device log. 
  - performanceTransactionForApplication: Start performance transaction collects data on duration, cpu, battery, memory of the specified application on the device.
  - report: Add a step to the generated report.
  - sendKeysWithBT: Send keyboard events to the Bluetooth keyboard on the device.
  - setAuthenticationReply: Simulate different authentication responses on applications that request a user fingerprint authentication.
  - setLocation: Set the current geolocation.
  - setReportStatus: Override the final report status.
  - simulateCapture: Simulate use of the camera by injecting an image file to the camera preview screen.
  - startStepsGroup: Start a group mark.
  - stopStepsGroup: End a group mark.


## Documentation
To learn more about Testing Cloud usage, features and best practices, see [Test Execution Home](https://docs.digital.ai/bundle/TE/page/test_execution_home.html)

## Support
If you encounter an issue that is not covered here or in our online documentation, contact us at [support@digital.ai](mailto:support@digital.ai).