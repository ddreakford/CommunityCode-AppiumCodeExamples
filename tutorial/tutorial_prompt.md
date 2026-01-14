# Prompt: Getting Started with Digital.ai Testing Lab

Let's write a tutorial named "Getting Started with Digital Testing". 

The tutorial should:

1) Give the reader a brief introduction to "Digital.ai Testing Lab" based on the following:
    - The content of this file: @platform_datasheet/DAI_Testing_Platform_Benefits_1.md
    - These images, which describe the Digital.ai Testing offering, device options and deployment options: @tutorial/DAI_Testing_Core_Offering.png, @tutorial/DAI_Testing_Device_Options.png, @tutorial/DAI_Testing_Deployment_Options.png

2) Refer the reader to this GitHub repo: https://github.com/ddreakford/CommunityCode-AppiumCodeExamples.git and tell the user we will use the "Containerized Testing" option described in https://github.com/ddreakford/CommunityCode-AppiumCodeExamples/blob/master/README.md

3) Instruct reader to:
    1. Clone the GitHub repo: https://github.com/ddreakford/CommunityCode-AppiumCodeExamples.git
    2. Read this section of the repo README.md : "Quick start: Containerized option (Recommended)"
    3. Copy the file `.env.example` to `.env`
    4. Update the values for these variables: `CLOUD_URL` and `ACCESS_KEY`
    5. (If necessary) update the values for the variables: `ANDROID_DEVICE_QUERY` and `IOS_DEVICE_QUERY`
    6. Using the "Docker Compose" section (README.md#docker-compose):
        - 1) Build the test code image
        - 2) Run the Java Quickstart tests, using this command: `docker-compose run --rm appium-tests --java --suites=testng_quickstart.xml`
    7. While the tests are running, observe the live visual indication of all of the following via the Test Execution dashboard (example URL: https://uscloud.experitest.com/#/execution/grid).
        - Devices being selected
        - Tests being run against the target application on the devices
        - Video being captured on the devices.

        Note: For every test that is run against the target application, on every device, a comprehensive report of the test steps, pass/fail status and accompanying video are captured. This makes troubleshooting and evaluation of your quality posture complete and comprehensive.

    8. Review the test run results (example URL: https://uscloud.experitest.com/reporter/reporter/tests). Filters can be created and reused to slice, dice and group test run results in support of your team's quality process.
