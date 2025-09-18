# Containerized Testing Guide

This guide explains how to run Appium tests in a containerized environment with support for parallel execution and comprehensive reporting.

## Overview

The containerized test environment provides:
- **Isolated execution**: Consistent test environment across all machines
- **Parallel processing**: Configurable parallel test execution
- **Multi-language support**: Java/TestNG and Python/pytest tests
- **Comprehensive reporting**: HTML reports and execution summaries
- **Digital.ai Testing Cloud integration**: Seamless cloud connectivity
- **Docker Hub ready**: Optimized for CI/CD pipelines

## Quick Start

### Prerequisites
- Docker Engine 20.10+ 
- Docker Compose 2.0+ (optional but recommended)
- Digital.ai Testing Cloud access credentials

### Environment Setup

1. **Copy environment template:**
   ```bash
   cp .env.example .env
   ```

2. **Configure your .env file:**
   ```bash
   # Required for cloud testing
   CLOUD_URL=https://your-company.experitest.com
   ACCESS_KEY=your_access_key_here
   APPIUM_VERSION=2.0.0
   
   # Device queries
   ANDROID_DEVICE_QUERY=@os='android' and contains(@name, 'Galaxy S2')
   IOS_DEVICE_QUERY=@os='ios' and contains(@name, 'Pro US')
   ```

### Running Tests

#### Option 1: Docker Compose
```bash
# (Re)Build the test code images
docker-compose build

# Run all tests with default settings
docker-compose up

# Run predefined test groups
docker-compose run --rm appium-tests --java --tests=quickstart

# Run specific test suites
docker-compose run --rm appium-tests --java --suites testng.xml,quickstart.xml

# TODO: Run platform-specific tests
# *Not yet implemented*
docker-compose run --rm appium-tests --all --platform=android
```

#### Option 2: Direct Docker
📋 Comprehensive reports and logs are provided by Digital.ai Testing cloud.
Examples for optional mounting of `reports` and `logs` directories are included here.

```bash
# Build the image
docker build -t appium-code-examples .

# Run specific test suites; optionally specify tests within the suites
docker run --rm --env-file .env appium-code-examples --java --suites testng_quickstart.xml
docker run --rm --env-file .env appium-code-examples --java --suites testng_quickstart.xml --tests=AndroidQuickStartTest

# Run all tests; mount reports and logs dirs
docker run --rm --env-file .env -v $(pwd)/reports:/app/reports -v $(pwd)/logs:/app/logs appium-code-examples --all
docker run --rm --env-file .env -v $(pwd)/reports:/app/reports -v $(pwd)/logs:/app/logs appium-code-examples --java --tests=quickstart

# Run with specific parameters
docker run --rm --env-file .env appium-code-examples --java --parallel=4
```

## Test Runner Commands

The Python test runner (`scripts/run_tests.py`) supports various execution modes:

### Basic Usage
```bash
python run_tests.py [OPTIONS]
```

### Command Line Options

| Option | Description | Example |
|--------|-------------|---------|
| `--all` | Run all tests (Java and Python) | `--all --parallel=4` |
| `--java` | Run Java/TestNG tests only | `--java --tests=quickstart` |
| `--suites=FILTER` | Specify suite definition files | `--java --suites=testng.xml,quickstart.xml` |
| `--tests=FILTER` | Filter tests by name | `--tests=quickstart` |
| `--python` | Run Python/pytest tests only | `--python --platform=android` |
| `--platform=FILTER` | Filter by platform | `--platform=android` |
| `--parallel=N` | Number of parallel workers (default: 4) | `--parallel=6` |
| `--generate-reports-only` | Generate reports from existing logs | `--generate-reports-only` |
| `--help` | Show help message | `--help` |

### Examples

```bash
# Conservative execution (development/laptops)
python run_tests.py --all --parallel=2

# Moderate execution (CI/CD pipelines)
python run_tests.py --all --parallel=4

# High-performance execution (powerful servers)
python run_tests.py --all --parallel=8

# Run only Java quick start tests with parallelism
python run_tests.py --java --tests=quickstart --parallel=4

# Run Python Android tests with high parallelism
python run_tests.py --python --platform=android --parallel=6
```

## Test Filtering

### Available Test Filters

#### Test Type Filters (`--tests`)
- `quickstart`: Basic Appium functionality tests
- `advanced`: Complex Appium operations
- `optional`: Platform-specific optional capabilities

#### Custom Filters (`--tests`)
You can also use custom patterns that match test class names or methods.
- `[TEST_NAME_FILTER]`: Matches test within a suite definition file

#### Platform Filters (`--platform`)
- `android`: Android-specific tests only
- `ios`: iOS-specific tests only

## Environment Configuration

### Required Environment Variables

| Variable | Description | Example |
|----------|-------------|---------|
| `CLOUD_URL` | Digital.ai Testing Cloud URL | `https://company.experitest.com` |
| `ACCESS_KEY` | Digital.ai access key | `aut_1_xxxx...` |
| `APPIUM_VERSION` | Appium version to use | `2.0.0` |

### Device Query Configuration
```bash
# Specify complete queries
ANDROID_DEVICE_QUERY=@os='android' and @model='Pixel.*' and @osVersion='13.*'
IOS_DEVICE_QUERY=@os='ios' and @model='iPhone 14.*'
```

## Reports and Logs

### Report Locations
- **HTML Reports**: `./reports/` directory
  - `./reports/java/` - Java/TestNG reports
  - `./reports/python/` - Python/pytest reports  
  - `./reports/test_summary.html` - Consolidated summary
- **JSON Summary**: `./reports/test_summary.json`
- **Execution Logs**: `./logs/` directory

### Report Features
- **Real-time output**: Live test execution streaming
- **HTML formatting**: Rich, interactive reports
- **Test timing**: Execution duration tracking
- **Error details**: Comprehensive failure information
- **Summary statistics**: Pass/fail rates and trends

## Development Mode

For development and debugging, use the development profile:

```bash
# Start development container with full project mounted
docker-compose --profile dev run --rm appium-tests-dev

# Inside the container, run tests manually
python3.11 scripts/run_tests.py --java --tests=quickstart
```

Development mode provides:
- Full project source mounting
- Interactive shell access
- Gradle and uv cache persistence
- Debugging capabilities

## CI/CD Integration

### GitHub Actions Example
```yaml
name: Appium Tests
on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Run Appium Tests
        env:
          CLOUD_URL: ${{ secrets.CLOUD_URL }}
          ACCESS_KEY: ${{ secrets.ACCESS_KEY }}
          APPIUM_VERSION: "2.0.0"
        run: |
          docker-compose run --rm appium-tests --all --parallel=4
      
      - name: Upload Reports
        uses: actions/upload-artifact@v3
        if: always()
        with:
          name: test-reports
          path: reports/
```

### Jenkins Pipeline Example
```groovy
pipeline {
    agent any
    environment {
        CLOUD_URL = credentials('digital-ai-cloud-url')
        ACCESS_KEY = credentials('digital-ai-access-key')
        APPIUM_VERSION = '2.0.0'
    }
    stages {
        stage('Test') {
            steps {
                sh 'docker-compose run --rm appium-tests --all --parallel=4'
            }
        }
    }
    post {
        always {
            publishHTML([
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'reports',
                reportFiles: 'test_summary.html',
                reportName: 'Test Report'
            ])
        }
    }
}
```

## Troubleshooting

### Common Issues

#### Container Build Failures
```bash
# Clear Docker cache and rebuild
docker system prune -a
docker-compose build --no-cache
```

#### Permission Issues
```bash
# Fix report directory permissions
sudo chown -R $USER:$USER reports/ logs/
```

#### Network Connectivity
```bash
# Test cloud connectivity
docker run --env-file .env appium-code-examples python3.11 -c "
import os
print(f'Testing connection to: {os.getenv(\"CLOUD_URL\")}')
"
```

#### Memory Issues
```bash
# Increase Docker memory limits in Docker Desktop
# Or reduce parallel workers:
docker-compose run --rm appium-tests --all --parallel=2
```

### Debug Mode
```bash
# Run with verbose output
docker-compose run --rm appium-tests --all --parallel=1

# Access development container
docker-compose --profile dev run --rm appium-tests-dev bash
```

### Log Analysis
```bash
# View recent logs
tail -f logs/java_tests_*.log
tail -f logs/python_tests_*.log

# Search for specific errors
grep -i "error\|failed\|exception" logs/*.log
```

## Performance Tuning

### Parallel Execution Guidelines

The test runner implements **two-level parallelism** for maximum efficiency:

#### **Level 1: Test Suite Parallelism**
- Java and Python test suites run in separate parallel processes
- Controlled by the `--parallel=N` parameter

#### **Level 2: TestNG Method Parallelism** 
- Java tests execute multiple TestNG methods concurrently within each suite
- Uses TestNG's `parallel="methods"` configuration
- [For simplicity] Thread count matches the `--parallel` value

#### **Recommended Settings**
- **Local development**: `--parallel=2` to `--parallel=4`
  - Good balance between speed and system resources
- **CI/CD environments**: `--parallel=4` to `--parallel=8`
  - Optimal for automated build pipelines  
- **High-performance servers**: `--parallel=8` to `--parallel=16`
  - Maximize throughput on powerful hardware
- **Memory considerations**: ~512MB per parallel worker
  - Monitor system memory usage during execution

### Optimization Tips
1. **Use appropriate parallelism** based on available CPU cores
2. **Monitor memory usage** during execution
3. **Optimize test data** for faster execution
4. **Use specific test filters** to run targeted test suites
5. **Leverage Docker layer caching** for faster builds

## Docker Hub Deployment

### Building for Docker Hub
```bash
# Build multi-architecture image
docker buildx create --use
docker buildx build --platform linux/amd64,linux/arm64 -t your-org/appium-code-examples:latest --push .

# Tag versions
docker tag your-org/appium-code-examples:latest your-org/appium-code-examples:v1.0.0
docker push your-org/appium-code-examples:v1.0.0
```

### Using from Docker Hub
```bash
# Pull and run from Docker Hub
docker pull your-org/appium-code-examples:latest
docker run --env-file .env -v $(pwd)/reports:/app/reports your-org/appium-code-examples:latest --all
```

## Support

For issues and questions:
1. Check the [troubleshooting section](#troubleshooting)
2. Review container logs in `./logs/` directory
3. Ensure environment variables are correctly configured
4. Verify Digital.ai Testing Cloud connectivity
5. Check Docker system resources and limits

## Advanced Configuration

### Custom Docker Compose Override
Create `docker-compose.override.yml` for local customizations:

```yaml
version: '3.8'
services:
  appium-tests:
    environment:
      - CUSTOM_VAR=custom_value
    volumes:
      - ./custom-data:/app/custom-data
```

### Resource Limits
Adjust container resource limits in `docker-compose.yml`:

```yaml
deploy:
  resources:
    limits:
      cpus: '6.0'      # Increase CPU limit
      memory: 8G       # Increase memory limit
    reservations:
      cpus: '3.0'      # Increase CPU reservation
      memory: 4G       # Increase memory reservation
```