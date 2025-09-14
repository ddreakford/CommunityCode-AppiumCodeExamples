# TODO
- Environment build and validation
    - ✅ Direct Docker
    - ✅ Docker Compose
    - Native
- Documentation
    - ✅ Direct Docker Commands
        - Add command to build Docker container
    - ✅ Verify `env.example` content (after Quick Tests validation)
    - ✅ Verify format of commands listed via `run_tests.py --help`
    - ✅ Add logging param spec option to `run_tests.py` (-v $(pwd)/logs:/app/logs appium-code-example)
- Parallel test exection
    - ✅ Current approach relies on OS level thread executor. This is too coarse grained. 
    - ✅ Use TestNG capabilities for Java tests. 
    - *TODO:* Python/Pytest 
- Suite and Test selection
    - Initially stick with suites defined in XML files
        - Would be nice if these were JSON. Is that an option? If not, is it worthwhile to make it so?
    - Accept suite specification for Java/TestNG tests
        - ✅ Update python runner and `build.gradle` for specifation of test suite XML files via project property.
    - Create manual test suite to verify Suite and Test selection
        - ✅ Verify: Specify a suite def that is a specialized variant of the known `testng.xml`
        `docker build -t appium-code-examples-test .`
        `docker run --rm --env-file .env appium-code-examples-test --java --suites testng_quickstart.xml`
        - ✅ Verify: Specify specific test(s) within the variant
        `docker build -t appium-code-examples-test .`
        `docker run --rm --env-file .env appium-code-examples-test --java --suites testng_quickstart.xml`
        `docker run --rm --env-file .env appium-code-examples-test --java --suites testng_quickstart.xml --tests=AndroidQuickStartTest`
        `docker run --rm --env-file .env appium-code-examples-test --java --suites testng_quickstart.xml --tests=IOSQuickStartTest`
        - ✅ Verify: Specify multiple test suite files, with a test filter
        `docker build -t appium-code-examples-test .`
        `docker run --rm --env-file .env appium-code-examples-test --java --suites testng.xml,testng_quickstart.xml  --tests=IOSQuickStartTest`
    - ✅ Update pertinent usage documentation
        - Repo README
        - Containerized Testing README
        - Testrunner with `--help` param
    - Add Performance Transaction tests
        - Get existing performance test working
            - This will validate the test filtering
        - Move Performance tests code (separate project) into `advancedCapabilities` package.
            - Replace the existing performance test code, as we know the currently used works.
            - Convert to Gradle build/execution
            - Utilize .env file environment configuration
            - Ensure it's launchable from `run_tests.py`
    - Add Self Healing tests
        - ...
    - Add suite XML files for demo use cases:
        - Collections: `quickstart`, `advanced`, `optional`
        - Use cases: `perf-pageload`, `perf-e2e`
- Error handling
    - Gracefully exit when tests cannot start (container currently hangs when this occurs)
- ✅ Results reporting and logging
    -  ✅ Clarify meaning of summary results returned by the python orchestrator.
- "Later" (maybe :-)
    - Dynamic specification via combination of test code decorators and CLI params
    - Use in-code decorators to define TestNG test groups
    - Use properties files to specify parameters for tests where required. 
        - For example, the performance test suites specify per-test params for capture level, network profile to be used, etc.
    - Deprecate XML suite files


