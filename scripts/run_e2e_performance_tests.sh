#!/bin/bash

# Performance Testing Execution Script
# This script runs the ported E2E performance tests

# Get the directory of this script
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# Navigate to the project root (one level up from scripts directory)
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

echo "=== Running E2E Performance Tests ==="
echo "📁 Project root: $PROJECT_ROOT"
echo ""

# Change to project root directory
cd "$PROJECT_ROOT"

# Check if .env file exists
if [ ! -f ".env" ]; then
    echo "⚠️  WARNING: .env file not found. Please create one with the following variables:"
    echo "CLOUD_URL=https://your-cloud-url.experitest.com"
    echo "ACCESS_KEY=your_access_key_here"
    echo "IOS_DEVICE_QUERY=\"@os='ios' and contains(@name, 'iPhone')\""
    echo "ANDROID_DEVICE_QUERY=\"@os='android' and @category='PHONE'\""
    echo "APPIUM_VERSION=2.0.0"
    echo ""
fi

echo "📋 Available test suites:"
echo "1. E2E Flow Tests (Complete user journeys)"
echo "   Usage: $0 e2e/e2e_flow_tests.xml"
echo "2. Launch Application Tests (Application startup performance)"
echo "   Usage: $0 e2e/launch_application_tests.xml"
echo "3. Main TestNG Suite"
echo "   Usage: $0 testng.xml"
echo "4. QuickStart Tests"
echo "   Usage: $0 testng_quickstart.xml"
echo ""

# Default to launch application tests
SUITE=${1:-"e2e/launch_application_tests.xml"}

echo "🚀 Running test suite: $SUITE"
echo ""

# Run the tests
./gradlew test -Psuites="$SUITE"

echo ""
echo "✅ Test execution completed!"
echo "📊 Check the test reports in build/reports/tests/test/"
echo "🗂️  HAR files (if generated) can be found in har_files/"