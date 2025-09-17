package advancedCommands.performanceTransaction.e2e.tests;

import advancedCommands.performanceTransaction.e2e.helpers.PerformanceHelpers;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import utils.EnvironmentConfig;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * Launch Application Performance Testing for Android
 *
 * This test focuses specifically on measuring the performance of launching the application,
 * extracting detailed metrics like Speed Index, CPU usage, Memory consumption, and Battery usage.
 */
public class LaunchApplicationAndroidTest {

    protected AndroidDriver driver = null;
    protected WebDriverWait wait = null;
    protected PerformanceHelpers helper;

    protected String speedIndex = null;
    protected String cpuAvg = null;
    protected String cpuMax = null;
    protected String memAvg = null;
    protected String memMax = null;
    protected String batteryAvg = null;
    protected String batteryMax = null;

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setApp("cloud:com.experitest.ExperiBank/.LoginActivity")
                .setAppPackage("com.experitest.ExperiBank")
                .setAppActivity(".LoginActivity")
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", EnvironmentConfig.getAndroidDeviceQuery())
                .amend("digitalai:testName", "Launch Application Performance Test - Android")
                .amend("digitalai:instrumentApp", true);

        driver = new AndroidDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        helper = new PerformanceHelpers(driver);
    }

    @Test
    @Parameters({"nvProfile", "captureLevel"})
    public void test_launch_of_application_response(@Optional("4G-average") String nvProfile,
                                                   @Optional("Application") String captureLevel) {

        try {
            // Start a group that will contain the individual test steps until 'endGroupingOfSteps' is called
            helper.startGroupingOfSteps("Launch Application Performance");

            // Start Performance Transaction Capturing
            helper.startCapturePerformanceMetrics(nvProfile, captureLevel, "com.experitest.ExperiBank");

            // Verify user landed on the Login page
            wait.until(ExpectedConditions.elementToBeClickable(By.id("com.experitest.ExperiBank:id/usernameTextField")));

            // End the Performance Transaction Capturing
            String response = helper.endCapturePerformanceMetrics("Launch Application Performance");

            // Ends the group that was started by 'startGroupingOfSteps'. In the Report we can now expand a group to see a set of steps within the group
            helper.endGroupingOfSteps();

            // Extract relevant properties from the Performance Transaction Response
            String link = helper.getPropertyFromPerformanceTransactionReport(response, "link");
            // Extract relevant properties from the Performance Transaction Response
            String transactionId = helper.getPropertyFromPerformanceTransactionReport(response, "transactionId");

            // Waiting few seconds to allow next API call to have some time for the data to accumulate after Transaction ends
            Thread.sleep(5000);

            // Extract relevant properties from the Performance Transaction API Response
            speedIndex = helper.getPropertyFromPerformanceTransactionAPI(transactionId, "speedIndex");
            cpuAvg = helper.getPropertyFromPerformanceTransactionAPI(transactionId, "cpuAvg");
            cpuMax = helper.getPropertyFromPerformanceTransactionAPI(transactionId, "cpuMax");
            memAvg = helper.getPropertyFromPerformanceTransactionAPI(transactionId, "memAvg");
            memMax = helper.getPropertyFromPerformanceTransactionAPI(transactionId, "memMax");
            batteryAvg = helper.getPropertyFromPerformanceTransactionAPI(transactionId, "batteryAvg");
            batteryMax = helper.getPropertyFromPerformanceTransactionAPI(transactionId, "batteryMax");

            // Add a custom step to the Automated Test Results with a link reference to the Performance Transaction Report
            helper.addReportStep(link);
            helper.addReportStep("Total Time to Launch & Load Application in ms: " + speedIndex);
        } catch (Exception e) {
            System.out.println("Something went wrong in Launch Application test");
            e.printStackTrace();
        }

        // Add custom properties that allow for easier filtering for the Automated Test Results
        helper.addPropertyForReporting("nvProfile", nvProfile);
        helper.addPropertyForReporting("captureLevel", captureLevel);
        helper.addPropertyForReporting("speedIndex", speedIndex);
        helper.addPropertyForReporting("cpuAvg", cpuAvg);
        helper.addPropertyForReporting("cpuMax", cpuMax);
        helper.addPropertyForReporting("memAvg", memAvg);
        helper.addPropertyForReporting("memMax", memMax);
        helper.addPropertyForReporting("batteryAvg", batteryAvg);
        helper.addPropertyForReporting("batteryMax", batteryMax);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}