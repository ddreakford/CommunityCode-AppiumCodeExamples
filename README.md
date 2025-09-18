# **Appium Open Source with Digital.ai Testing - Code Examples**
This project demonstrates how to run Appium based tests on devices hosted by Digital.ai Testing Cloud. Samples are included for Java/TestNG and Python/pytest implementations. 

While the "containerized" option documented below is preferred, instructions are provided for natively installed dependencies, as well.

## **Requirements**

### Containerized Testing (Recommended)
- **Docker**: Docker Engine 20.10+
- **Docker Compose**: 2.0+ (optional)
- **Digital.ai Testing Cloud**: Access credentials

### Local Test Development (natively installed dependencies)
- **Java**: JDK 11+, Gradle
- **Python**: Python 3.9+, uv package manager

### Local Appium Server (optional)
A local Appium server is optional, as tests can be seamlesly run against devices hosted in the Digital.ai Testing Cloud whether dependencies are containerized or managed natively.
- **Appium**: Appium server running on localhost:4723

## **Application Under Test**
* The tests in this repo are generally run on two applications: Eribank and UIcatalog. The tests assume the applicatin under test is available for installation on the target device. If you are using Digital.ai Testing Cloud and the application has been made available to your project, application installion on the target device can be triggered via Capabilities that are specified for the test.

## **Project Structure**

```
CommunityCode-AppiumCodeExamples/
├── architecture/              # Architectural notes, guides and env validation tests
│   ├── ...                
├── java/                      # Java/TestNG tests
│   ├── build.gradle           # Java build configuration
│   ├── testng.xml             # TestNG suite configuration
│   └── src/test/              # Java test sources
│       ├── java/              # Test classes
│       └── resources/         # Test resources (suite files, images, etc.)
├── python/                    # Python/pytest tests [@TODO / NOT YET IMPLEMENTED]
│   ├── pyproject.toml         # Python project & dependencies
│   ├── conftest.py            # Pytest configuration & fixtures
│   └── tests/                 # Python test sources
├── scripts/                   # Test execution scripts
│   └── run_tests.py           # Python test runner with parallel execution
├── reports/                   # Framework generated reports (e.g. TestNG, pytest)
├── logs/                      # Framework generated test logs
├── shared/                    # Shared test resources
├── Dockerfile                 # Test environment container definition
├── docker-compose.yml         # Test environment container orchestration
├── .dockerignore              # Docker build context optimization
├── .env.example               # Test environment configuration template
└── build.gradle               # Build/test configuration (Gradle driving TestNG, pytest)
```

## **Quick start: Containerized option**
Use this option to run tests without separately installing all of the required dependencies.

📋 See [CONTAINER_TESTING_GUIDE.md](/architecture/CONTAINER_TESTING_GUIDE.md) for full documentation of this option.

### Prereq: Environment variables
 **Recommended:** Create a `.env` file as documented in [CONTAINER_TESTING_GUIDE.md](/architecture/CONTAINER_TESTING_GUIDE.md)


### Docker Compose
```bash
# Run specific test suites
docker-compose build # (Re)Build the test code images
docker-compose run --rm appium-tests --java --tests=quickstart
docker-compose run --rm appium-tests --python --platform=android
docker-compose run --rm appium-tests --all --parallel=6  # Run with 6 parallel workers
```

### Direct Docker
📋 Comprehensive reports and logs are provided by Digital.ai Testing cloud.
See [CONTAINER_TESTING_GUIDE.md](/architecture/CONTAINER_TESTING_GUIDE.md) for optional mounting
of `reports` and `logs` directories.

```bash
# Build the image
docker build -t appium-code-examples .

# Get help and see all available options
docker run --rm appium-code-examples --help

# Run all tests with environment file
docker run --rm --env-file .env --all --parallel=4  # 4 parallel workers

# Run specific test types
docker run --rm --env-file .env --java --tests=quickstart
docker run --rm --env-file .env --python --platform=android

# Available options:
# --all                    Run all tests (Java and Python)
# --java                   Run Java/TestNG tests only  
# --python                 Run Python/pytest tests only
# --tests=FILTER          Filter tests (quickstart, advanced, optional)
# --platform=FILTER       Filter by platform (android, ios)
# --parallel=N            Number of parallel workers (default: 4)
```

## **Parallel Test Execution**

The test runner uses two levels of parallelism:

### **Process Level**
- Java and Python suites can run simultaneously in separate processes
- Controlled by `--parallel=N` parameter (default: 4 workers)
  - Gradle `maxParallelForks` is set to this value

### **Framework Level** 
- Java tests use TestNG `parallel="methods"` for concurrent test method execution
- Python [@TODO / NOT YET IMPLEMENTED]

### **Parallel Execution Guidelines**
- **Local development**: `--parallel=2` to `--parallel=4`
- **CI/CD environments**: `--parallel=4` to `--parallel=8` 
- **High-performance servers**: `--parallel=8+`
- **Memory consideration**: ~512MB per parallel worker

### **Examples**
```bash
# Conservative parallel execution (good for laptops)
docker-compose run --rm appium-tests --all --parallel=2

# Moderate parallel execution (good for CI/CD)
docker-compose run --rm appium-tests --all --parallel=4

# High parallel execution (powerful servers)
docker-compose run --rm appium-tests --all --parallel=8
```

## **How the test code is organized**

### **Android and iOS Testing**
Most commands and capabilities have two implementations: one for Android and the other for IOS.

### Package & Classes Details

#### **quickStartTests**
This package includes basic tests for Android and ios. Some basic Appium commands are present in this package such as click(), findElement(), and sendKeys().

#### **optionalCapabilities**
This package uses optional capabilities that can be used to run the Appium test. Aside from the mandatory capabilities, you can set the following optional capabilities:
- buildVersion: Install/launch the app by build version.
- commandScreenshot: In video report, take a screenshot before and after every action when commandScreenshot is set to true.
- doNotGoHomeOnQuit: Device remains in the last state even after ending the test when dontGoHomeOnQuit is set to true.
- installOnlyForUpdate: Install the app only if an older version is already installed on the device.
- releaseDevice: Gives the capability to not release a device after performing driver.quit().
- releaseVersion: Install/launch the app by release version.
- reportDisable: Do not generate a report.

#### **advancedCommands**
These are special commands using the executeScript command. 
  - adbPushFile: Perform the ADB push file to device command.
  - adbPullFile: Perform the ADB pull file from device command.
  - audioPlay: Play an audio file.
  - audioRecording: Record to a file, from the device.
  - installApp: Install the application with given name/unique name/build version/release version/both build and release version.
  - startLoggingDevice: Start the device log. The log is written to the path provided by the user.
  - stopLoggingDevice: Stop the device log. 
  - performanceTransaction: Start performance transaction collects data on duration, cpu, battery, memory of the specified application on the device.
  - report: Add a step to the generated report.
  - sendKeysWithBT: Send keyboard events to the Bluetooth keyboard on the device.
  - setAuthenticationReply: Simulate different authentication responses on applications that request a user fingerprint authentication.
  - setLocation: Set the current geolocation.
  - setReportStatus: Override the final report status.
  - simulateCapture: Simulate use of the camera by injecting an image file to the camera preview screen.
  - startStepsGroup: Start a group mark.
  - stopStepsGroup: End a group mark.


## **Quick start (with natively installed dependencies)**

### Java/TestNG tests
```bash
# Run Java tests
./gradlew javaTest

# Or from java directory
cd java && ./gradlew test
```

### Python/pytest tests
```bash
# Install dependencies and run Python tests
./gradlew pythonTest

# Or manually with uv
cd python
uv sync
uv run pytest
```

### Run all tests
```bash
./gradlew test
```


## Documentation
To learn more about Testing Cloud usage, features and best practices, see [Test Execution Home](https://docs.digital.ai/bundle/TE/page/test_execution_home.html)

## Support
If you encounter an issue that is not covered here or in our online documentation, contact us at [support@digital.ai](mailto:support@digital.ai).