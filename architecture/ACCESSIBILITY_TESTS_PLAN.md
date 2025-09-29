# Accessibility Tests Implementation Plan

## Overview
Create two accessibility scan tests using Deque axe DevTools for Mobile integration - one for iOS and one for Android platforms.

## Project Analysis Summary

### Current Project Structure
- **Test Location**: `java/src/test/java/advancedCommands/`
- **Package Convention**: `advancedCommands.{feature-name}`
- **Template Files**:
  - iOS: `quickStartTests/IOSQuickStartTest.java`
  - Android: `quickStartTests/AndroidQuickStartTest.java`
- **Environment Config**: Uses `utils/EnvironmentConfig.java` with `.env` file support
- **Reports**: Stored in `reports/` directory (JSON and HTML formats)

## Implementation Plan

### 1. Environment Configuration Setup
- **Task**: Add `AXE_DEVTOOLS_API_KEY` to `.env.example`
- **Location**: `.env.example` file
- **Purpose**: Allow users to configure their Deque axe DevTools API key

### 2. Create Accessibility Tests Package Structure
- **Directory**: `java/src/test/java/advancedCommands/accessibility-scan/`
- **Package**: `advancedCommands.accessibility-scan`
- **Files to Create**:
  - `IOSAccessibilityTest.java`
  - `AndroidAccessibilityTest.java`

### 3. iOS Accessibility Test Implementation
- **File**: `IOSAccessibilityTest.java`
- **Template**: Based on `IOSQuickStartTest.java`
- **Key Features**:
  - Single test method: `accessibilityScan()`
  - Use `IOSDriver` with `XCUITestOptions`
  - Call `driver.executeScript("mobile: axeScan", settings)`
  - Settings object with `apiKey` and `scanName`
  - Error handling and result logging

### 4. Android Accessibility Test Implementation
- **File**: `AndroidAccessibilityTest.java`
- **Template**: Based on `AndroidQuickStartTest.java`
- **Key Features**:
  - Single test method: `accessibilityScan()`
  - Use `AndroidDriver` with `UiAutomator2Options`
  - Call `driver.executeScript("mobile: axeScan", settings)`
  - Settings object with `apiKey` and `scanName`
  - Error handling and result logging

### 5. Accessibility Results Utility
- **Purpose**: Shared utility for both iOS and Android tests
- **Location**: `java/src/test/java/utils/AccessibilityResultsUtil.java`
- **Functions**:
  - Save scan results to JSON file in `reports/` directory
  - Save scan results to HTML file in `reports/` directory
  - Follow project's existing reporting conventions
  - Handle file creation and error scenarios

### 6. Environment Configuration Enhancement
- **Task**: Add method to `EnvironmentConfig.java` for retrieving `AXE_DEVTOOLS_API_KEY`
- **Method**: `getAxeDevToolsApiKey()`
- **Validation**: Ensure API key is provided and not empty

## File Structure After Implementation

```
java/src/test/java/
├── advancedCommands/
│   ├── accessibility-scan/
│   │   ├── IOSAccessibilityTest.java
│   │   └── AndroidAccessibilityTest.java
│   └── [other existing commands...]
├── utils/
│   ├── EnvironmentConfig.java (enhanced)
│   ├── AccessibilityResultsUtil.java (new)
│   └── ApplicationUploadUtil.java
└── [other existing packages...]

reports/
├── accessibility/
│   ├── ios-accessibility-scan.json
│   ├── ios-accessibility-scan.html
│   ├── android-accessibility-scan.json
│   └── android-accessibility-scan.html
└── [other existing reports...]
```

## Dependencies and Requirements
- **Deque axe DevTools for Mobile**: Integration already assumed to be available
- **API Key**: Users must obtain and configure `AXE_DEVTOOLS_API_KEY`
- **Environment**: Both iOS and Android device support via Digital.ai Testing Cloud
- **Existing Dependencies**: Leverage current TestNG, Appium, and utility frameworks

## Testing Approach
- Each test will perform an accessibility scan on the application's current state
- Results will be captured and saved to both JSON and HTML formats
- Error handling will provide descriptive messages for troubleshooting
- Tests follow the existing project patterns for setup, execution, and teardown

## Setup Instructions (for documentation)
1. Add `AXE_DEVTOOLS_API_KEY=your_api_key_here` to your `.env` file
2. Ensure iOS and Android device queries are configured in `.env`
3. Run tests using existing TestNG configuration
4. Check `reports/accessibility/` for scan results