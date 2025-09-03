# Quick Tests - Containerized Appium Test Environment

This document provides quick tests to verify your containerized Appium test environment is working correctly.

## Prerequisites

- Docker installed and running
- Docker Compose installed (optional)
- Valid Digital.ai Testing Cloud credentials in `.env` file

## Quick Test Commands

### 1. Docker Container Build Test
```bash
docker build -t appium-code-examples .
```
**Expected**: 
- Build completes successfully without errors
- Image is created and available locally
- Multi-stage build processes Java and Python dependencies

**Verify the image exists**:
```bash
docker images appium-code-examples
```
**Expected**: Shows the newly built image with tag `latest`

### 2. Help Command Test
```bash
docker run --rm appium-code-examples --help
```
**Expected**: Displays help information with container usage examples

### 3. Environment Validation Test
```bash
docker run --rm --env-file .env  -v $(pwd)/reports:/app/reports -v $(pwd)/logs:/app/logs appium-code-examples --java --tests=quickStart
```
**Expected**: 
- Environment validation passes ✅
- Test compilation succeeds
- Connection attempts to Digital.ai Testing Cloud

### 4. Docker Compose Test
```bash
docker-compose run --rm appium-tests --java --tests=quickStart
```
**Expected**: Same as above but using Docker Compose orchestration

### 5. Parallel Execution Test
```bash
docker run --rm --env-file .env -v $(pwd)/reports:/app/reports -v $(pwd)/logs:/app/logs appium-code-examples --all --parallel=2
```
**Expected**: 
- Both Java and Python tests run in parallel
- HTML reports generated in `./reports/`
- Execution logs created in `./logs/`

### 6. Report Generation Verification
```bash
# After running tests, check reports
ls -la reports/
ls -la logs/
```
**Expected**:
- `reports/java/` with HTML test reports
- `reports/test_summary.html` and `test_summary.json`
- `logs/java_tests_*.log` with execution logs

## Troubleshooting Quick Checks

### Connection Issues
If tests timeout or fail to connect:

1. **Verify URL format**:
   ```bash
   grep CLOUD_URL .env
   ```
   Should be: `CLOUD_URL=https://uscloud.experitest.com` (no double protocol)

2. **Test DNS resolution**:
   ```bash
   docker run --rm alpine nslookup uscloud.experitest.com
   ```

3. **Verify access key format**:
   ```bash
   grep ACCESS_KEY .env
   ```
   Should start with `aut_` and be properly formatted

### Build Issues
If Docker build fails:

1. **Clean build**:
   ```bash
   docker build --no-cache -t appium-code-examples .
   ```

2. **Check disk space**:
   ```bash
   docker system df
   ```

### Performance Issues
If tests run slowly:

1. **Reduce parallel workers**:
   ```bash
   docker run --rm --env-file .env appium-code-examples --java --parallel=1
   ```

2. **Use local Docker cache**:
   ```bash
   docker-compose build --no-cache
   ```

## Expected Test Results

### ✅ Successful Connection (Current Status)
- Tests create Appium sessions successfully
- Device queries find matching devices in Digital.ai Testing Cloud
- Test execution reaches actual test logic and runs for several minutes
- HTML reports show detailed test execution results
- Custom device queries with `contains()` functions work correctly

### ❌ Previous Connection Issues (Now Resolved)
- ~~`UnresolvedAddressException`: URL format issue~~ **FIXED** ✅
- ~~`ConnectException`: Network connectivity issue~~ **FIXED** ✅  
- ~~Device query validation errors~~ **FIXED** ✅

### Current Test Execution Behavior
- **Android Platform**: Tests execute successfully and run for several minutes
- **iOS Platform**: Tests connect but may encounter device-specific runtime errors
- **Advanced Commands**: Some may show `UnsupportedCommandException` depending on device capabilities
- **Quick Start Tests**: Best for initial validation of connectivity

### Infrastructure Success Indicators
- ✅ Environment validation passes
- ✅ Java compilation succeeds  
- ✅ Python dependencies resolve
- ✅ HTML reports generated
- ✅ Logs created with proper timestamps
- ✅ Parallel execution works
- ✅ Container cleanup after execution

## Quick Verification Checklist

- [ ] Help command displays container usage examples
- [ ] Environment variables load correctly from `.env` file
- [ ] Java tests compile without JUnit Jupiter errors
- [ ] Python tests load without dependency issues
- [ ] HTML reports generate in `./reports/` directory
- [ ] Execution logs create in `./logs/` directory
- [ ] Docker Compose works with custom parameters
- [ ] Parallel execution runs multiple test suites
- [ ] Container cleanup happens after execution

## Next Steps

Once these quick tests pass, you can:

1. **Verify cloud connectivity** with Digital.ai Testing support
2. **Check device availability** for configured device queries
3. **Run full test suite** with valid cloud credentials
4. **Integrate with CI/CD** using the containerized environment

## Docker Hub Ready

The containerized environment is production-ready and can be:
- Tagged and pushed to Docker Hub
- Used in CI/CD pipelines
- Deployed to container orchestration platforms
- Scaled horizontally for parallel test execution