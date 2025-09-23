# TODO
- Environment build and validation
    - ✅ Direct Docker
    - ✅ Docker Compose
    - *TODO* *maybe*: Native
- Documentation and Organization
    - ✅ Direct Docker Commands
        - Add command to build Docker container
    - ✅ Verify `env.example` content (after Quick Tests validation)
    - ✅ Verify format of commands listed via `run_tests.py --help`
    - ✅ Add logging param spec option to `run_tests.py` (-v $(pwd)/logs:/app/logs appium-code-example)
- ✅ (RE)Fix suite selection
    - Having to specify `src/test/resources/...`
    - Simple XML file name should work; 
- Parallel test exection
    - ✅ Current approach relies on OS level thread executor. This is too coarse grained. 
    - ✅ Use TestNG capabilities for Java tests. 
    - *TODO*: Python/Pytest 
- Suite and Test selection
    - Initially stick with suites defined in XML files
        - Would be nice if these were JSON. Is that an option? If not, is it worthwhile to make it so?
    - Accept suite specification for Java/TestNG tests
        - ✅ Update python runner and `build.gradle` for specifation of test suite XML files via project property.
    - Create manual test suite to verify Suite and Test selection
        - ✅ Verify: Specify a suite def that is a specialized variant of the known `testng.xml`
        `docker build -t appium-code-examples-test .`
        `docker run --rm --env-file .env appium-code-examples-test --java --suites quickstart.xml`
        - ✅ Verify: Specify specific test(s) within the variant
        `docker build -t appium-code-examples-test .`
        `docker run --rm --env-file .env appium-code-examples-test --java --suites quickstart.xml`
        `docker run --rm --env-file .env appium-code-examples-test --java --suites quickstart.xml --tests=AndroidQuickStartTest`
        `docker run --rm --env-file .env appium-code-examples-test --java --suites quickstart.xml --tests=IOSQuickStartTest`
        - ✅ Verify: Specify multiple test suite files, with a test filter
        `docker build -t appium-code-examples-test .`
        `docker run --rm --env-file .env appium-code-examples-test --java --suites testng.xml,quickstart.xml  --tests=IOSQuickStartTest`
    - ✅ Update pertinent usage documentation
        - Repo README
        - Containerized Testing README
        - Testrunner with `--help` param
    - ✅ Add Performance Transaction tests
        - ✅ Get existing performance test working
            - Also: this further validates the test execution filtering
            `docker run --rm --env-file .env appium-code-examples --java --suites performance_trx_quickstart.xml`
        - ✅ Port "better" performance test samples into `advancedCapabilities` package
            - From [DAI Performance Tests on Mobile](https://github.com/raheekhandigitalai/DAI-CT-Performance-Tests-on-Mobile-Sample.git)
            - Convert to Gradle build/execution
            - Utilize .env file environment configuration
        - ✅ Fix E2E Payment Flow
            - Attempt to locate a UI XCUIElementTypeIcon[@name='SeeTestDemoApp']
            - Attempt to start a transaction that's already in progress
            - These issues were related; fixing the first obviated the other.
    - ✅ Add Self Healing tests
        - From [DAI AI-Powered Self Healing for Appium](https://github.com/dai-continuous-testing/CT-Self-Healing-Tests)
        - Convert to Gradle build/execution
        - Utilize .env file environment configuration
    - Add Accessibility / Deque Axe integration
        - License for Deque Axe is needed (?)
        - Demo --> Deque
        - Decide: Create dashboard within Digital to show it all under a single pane?
    - Add suite XML files for demo use cases:
        - Collections: `quickstart`, `advanced`, `optional`
        - Use cases: `perf-pageload`, `perf-e2e`
- Error handling
    - Gracefully exit when tests cannot start (container currently hangs when this occurs)
    - Appium Test Execution Summary
        - Reference detailed [Java/TestNG] log: 
            - Currently, only `.log` is referenced (example: `/app/logs/java_tests_1758037712.log`)
            - Add detailed test execution log: (example: `reports/java/index.html`)
- ✅ Results reporting and logging
    -  ✅ Clarify meaning of summary results returned by the python orchestrator.
- "Later" (maybe :-)
    - Dynamic specification via combination of test code decorators and CLI params
    - Use in-code decorators to define TestNG test groups
    - Use properties files to specify parameters for tests where required. 
        - For example, the performance test suites specify per-test params for capture level, network profile to be used, etc.
    - Deprecate XML suite files


