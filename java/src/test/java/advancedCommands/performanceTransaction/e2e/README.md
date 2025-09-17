# End-to-End Performance Testing with Digital.ai Continuous Testing

This directory contains comprehensive performance testing examples for mobile applications using Digital.ai's Continuous Testing platform. These tests demonstrate how to capture detailed performance metrics during functional Appium test execution.

## Overview

Performance Transactions allow you to capture metrics during user flows, providing insights into:
- **Speed Index** - Time to launch and load application
- **CPU Usage** - Average and maximum CPU consumption
- **Memory Usage** - Average and maximum memory consumption
- **Battery Usage** - Average and maximum battery consumption
- **Network Traffic** - HAR file analysis and network performance data

## Test Structure

### Helper Classes

- **PerformanceHelpers.java** - Utility class containing methods for:
  - Starting/stopping performance transaction capture
  - Extracting metrics from transaction responses
  - Downloading and analyzing HAR files
  - Report integration

### Test Classes

#### E2E Flow Tests
- **E2EFlowIOSTest.java** - Complete end-to-end user journey for iOS
- **E2EFlowAndroidTest.java** - Complete end-to-end user journey for Android

These tests demonstrate a complete user flow with 4 performance transactions:
1. **Launch Application** - Measures app startup performance
2. **Login Flow** - Captures login process performance
3. **Payment Flow** - Measures payment transaction performance
4. **Logout Flow** - Captures logout process performance

#### Launch Application Tests
- **LaunchApplicationIOSTest.java** - Focused application launch testing for iOS
- **LaunchApplicationAndroidTest.java** - Focused application launch testing for Android

These tests focus specifically on application startup performance and extract detailed metrics via API calls.

## Configuration

### Environment Variables

Configure the following environment variables in your `.env` file:

```bash
# Digital.ai Cloud Configuration
CLOUD_URL=https://your-cloud-url.experitest.com
ACCESS_KEY=your_access_key_here

# Device Queries
IOS_DEVICE_QUERY="@os='ios' and contains(@name, 'iPhone')"
ANDROID_DEVICE_QUERY="@os='android' and @category='PHONE'"

# Appium Version
APPIUM_VERSION=2.0.0
```

### TestNG Suites

Two pre-configured TestNG suites are available:

1. **e2e_flow_tests.xml** - Runs complete E2E flows for both platforms
2. **launch_application_tests.xml** - Runs launch performance tests with various network profiles

## Running Tests

### Using Gradle

Run E2E flow tests:
```bash
cd java
./gradlew test -Psuites=../src/test/resources/e2e/e2e_flow_tests.xml
```

Run launch application tests:
```bash
cd java
./gradlew test -Psuites=../src/test/resources/e2e/launch_application_tests.xml
```

### Network Profiles

Tests support various network profiles:
- `4G-average` - Standard 4G connection
- `3G-average` - Standard 3G connection
- `4G-bad-connection` - Poor 4G connection for stress testing

### Capture Levels

Performance data can be captured at two levels:
- `Application` - Application-specific metrics
- `Device` - Device-level metrics

## Performance Metrics

### Available Metrics

The following metrics are automatically captured and can be extracted:

**From Transaction Response:**
- `transactionName` - Name of the performance transaction
- `transactionId` - Unique identifier for the transaction
- `appName` - Application name
- `appVersion` - Application version
- `link` - Direct link to performance transaction report

**From API Response:**
- `networkProfile` - Network profile used during test
- `cpuAvg` / `cpuMax` - CPU usage statistics
- `memAvg` / `memMax` - Memory usage statistics
- `batteryAvg` / `batteryMax` - Battery usage statistics
- `duration` - Transaction duration
- `speedIndex` - Speed index measurement

### HAR File Analysis

The framework includes functionality to download and analyze HAR files for detailed network analysis:

```java
// Extract network metrics from HAR file
ArrayList<String> metrics = helper.extractHARFileMetrics(transactionId, "test_name");
```

This provides insights into:
- Number of network calls made during transaction
- Total time spent on network calls
- Individual request/response timing data

## Integration with Reports

Performance transaction reports are automatically linked to functional test reports, providing:

- Direct links to performance dashboards
- Custom properties for easy filtering
- Grouped test steps for better organization
- Pass/fail status integration

## Dependencies

The following dependencies are required and included in the Gradle build:

```gradle
// Performance testing dependencies
testImplementation group: 'org.json', name: 'json', version: '20231013'
testImplementation group: 'org.apache.httpcomponents', name: 'httpclient', version: '4.5.14'
testImplementation group: 'com.konghq', name: 'unirest-java', version: '3.14.5'
```

## Best Practices

1. **Wait Time** - Allow 5 seconds after ending a transaction before making API calls for metric extraction
2. **Error Handling** - Wrap performance transactions in try-catch blocks
3. **Reporting** - Use step grouping to organize related test steps
4. **Properties** - Add custom properties for easy test result filtering
5. **Resource Cleanup** - Ensure HAR files are properly managed to avoid disk space issues

## Troubleshooting

### Common Issues

1. **API Authentication** - Ensure `ACCESS_KEY` is correctly configured
2. **Device Queries** - Verify device queries match available devices in your cloud
3. **Network Profiles** - Confirm network profiles exist in your Digital.ai environment
4. **HAR File Access** - Check that HAR file download permissions are enabled

### Debug Tips

- Enable verbose logging for Appium driver
- Verify environment configuration with `EnvironmentConfig` utility
- Check Digital.ai cloud console for real-time test execution
- Review generated HAR files for network troubleshooting

## Resources

- [Digital.ai Performance Transaction Documentation](https://docs.experitest.com/display/TE/StartPerformanceTransactionForApplication)
- [Transaction Report Documentation](https://docs.experitest.com/display/TE/Transaction+report)
- [Transaction View Documentation](https://docs.experitest.com/display/TE/Transaction+View)