# Containerized Test Environment Prompt
This is the initial prompt used to jumpstart creation of this version of the Appium OSS Code Examples project.

Please create a similar utility that can be used for this project. 
Please review the following requirements, recommend improvements, create a plan and then check with me before creating the new utility and other assets that we will need.

- Support containerized test execution. Create a Dockerfile in this project for an image that will provide or download the tools that are currently being used in this project to run tests.
- Do not read configuration settings from a `config.properties` file. Use environment variables.
- Create a python script that configures necessary settings and calls the required commands to execute the tests. Use python instead of a shell script to drive the process.
- Continue to use Gradle to run the tests in this project. I don't want to use Maven.
- Support parallel test execution.
- Support the specification of specific tests, or the execution of all tests.

Document the requirements for running tests in this setup in a markdown file. You can update `README.md` or you can create a separate file and refer to the separate file from `README.md`. 
