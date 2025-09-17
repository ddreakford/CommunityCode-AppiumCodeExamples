package advancedCommands.performanceTransaction.e2e.tests;

import advancedCommands.performanceTransaction.e2e.helpers.PerformanceHelpers;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.openqa.selenium.By;
import utils.EnvironmentConfig;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * End-to-End Performance Testing Flow for Android
 *
 * This test demonstrates how to capture Performance Metrics for Mobile Tests using Digital.ai's Continuous Testing solution.
 * In the Report, we capture data such as Average & Maximum consumed CPU / Memory / Battery, as well as
 * Network Traffic, Speed Index, and if applicable, download / upload speed for the Network Profile applied.
 *
 * Performance Transactions can be seen as user flows that we want to capture, for example:
 * - Launching an Application
 * - Logging into an Application
 * - Making a Payment
 * - Logging out
 *
 * Each can be considered individual Performance Transactions.
 */
public class E2EFlowAndroidTest {

    protected AndroidDriver driver = null;
    protected WebDriverWait wait;
    protected PerformanceHelpers helper;

    @BeforeClass
    public void setUp() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setApp("cloud:com.experitest.ExperiBank/.LoginActivity")
                .setAppPackage("com.experitest.ExperiBank")
                .setAppActivity(".LoginActivity")
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", EnvironmentConfig.getAndroidDeviceQuery())
                .amend("digitalai:testName", "EriBank E2E Payment Flow - Android")
                .amend("digitalai:instrumentApp", true);

        driver = new AndroidDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        helper = new PerformanceHelpers(driver);
    }

    @Test(priority = 1)
    @Parameters({"nvProfile", "captureLevel"})
    public void test_launch_of_application_response(@Optional("4G-average") String nvProfile,
                                                   @Optional("Application") String captureLevel) {
        try {
            // Start a group that will contain the individual test steps until 'endGroupingOfSteps' is called
            helper.startGroupingOfSteps("Launch Application");

            // // Install the Application only
            // driver.executeScript("seetest:client.install", "cloud:com.experitest.ExperiBank", "false", "false");

            // Start Performance Transaction Capturing
            helper.startCapturePerformanceMetrics(nvProfile, captureLevel, "com.experitest.ExperiBank");

            // Click on the EriBank icon on the Device Home Page
            driver.findElement(By.xpath("//android.widget.TextView[@text='SeeTestDemoApp']")).click();

            // Verify user landed on the Login page
            wait.until(ExpectedConditions.elementToBeClickable(By.id("com.experitest.ExperiBank:id/usernameTextField")));

            // End the Performance Transaction Capturing
            String response = helper.endCapturePerformanceMetrics("Launch Application");

            // Ends the group that was started by 'startGroupingOfSteps'. In the Report we can now expand a group to see a set of steps within the group
            helper.endGroupingOfSteps();

            // Extract relevant properties from the Transaction Response
            String link = helper.getPropertyFromPerformanceTransactionReport(response, "link");

            // Add a custom step to the Automated Test Results with a link reference to the Performance Transaction Report
            helper.addReportStep(link);
        } catch (Exception e) {
            System.out.println("Something went wrong in Launch Application test");
            e.printStackTrace();
        }

        // Add custom properties that allow for easier filtering for the Automated Test Results
        helper.addPropertyForReporting("nvProfile", nvProfile);
        helper.addPropertyForReporting("captureLevel", captureLevel);
    }

    @Test(priority = 2, dependsOnMethods = {"test_launch_of_application_response"})
    @Parameters({"nvProfile", "captureLevel"})
    public void test_login_response_time(@Optional("4G-average") String nvProfile,
                                       @Optional("Application") String captureLevel) {
        try {
            helper.startGroupingOfSteps("Login Flow");

            driver.findElement(By.id("com.experitest.ExperiBank:id/usernameTextField")).sendKeys("company");
            driver.findElement(By.id("com.experitest.ExperiBank:id/passwordTextField")).sendKeys("company");

            helper.startCapturePerformanceMetrics(nvProfile, captureLevel, "com.experitest.ExperiBank");

            driver.findElement(By.id("com.experitest.ExperiBank:id/loginButton")).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.id("com.experitest.ExperiBank:id/makePaymentButton")));

            String response = helper.endCapturePerformanceMetrics("Login Flow");

            helper.endGroupingOfSteps();

            String link = helper.getPropertyFromPerformanceTransactionReport(response, "link");
            helper.addReportStep(link);
        } catch (Exception e) {
            System.out.println("Something went wrong in Login test");
            e.printStackTrace();
        }

        helper.addPropertyForReporting("nvProfile", nvProfile);
        helper.addPropertyForReporting("captureLevel", captureLevel);
    }

    @Test(priority = 3, dependsOnMethods = {"test_login_response_time"})
    @Parameters({"nvProfile", "captureLevel"})
    public void test_payment_response_time(@Optional("4G-average") String nvProfile,
                                         @Optional("Application") String captureLevel) {
        try {
            helper.startGroupingOfSteps("Payment Flow");

            driver.findElement(By.id("com.experitest.ExperiBank:id/makePaymentButton")).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.id("com.experitest.ExperiBank:id/phoneTextField")));

            driver.findElement(By.id("com.experitest.ExperiBank:id/phoneTextField")).sendKeys("3479350000");
            driver.findElement(By.id("com.experitest.ExperiBank:id/nameTextField")).sendKeys("Rahee");
            driver.findElement(By.id("com.experitest.ExperiBank:id/amountTextField")).sendKeys("20");
            driver.findElement(By.id("com.experitest.ExperiBank:id/countryButton")).click();

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.widget.TextView[@text='Switzerland']")));
            driver.findElement(By.xpath("//android.widget.TextView[@text='Switzerland']")).click();

            helper.startCapturePerformanceMetrics(nvProfile, captureLevel, "com.experitest.ExperiBank");

            driver.findElement(By.id("com.experitest.ExperiBank:id/sendPaymentButton")).click();
            driver.findElement(By.id("android:id/button1")).click(); // "Yes" button

            wait.until(ExpectedConditions.elementToBeClickable(By.id("com.experitest.ExperiBank:id/makePaymentButton")));

            String response = helper.endCapturePerformanceMetrics("Payment Flow");

            helper.endGroupingOfSteps();

            String link = helper.getPropertyFromPerformanceTransactionReport(response, "link");
            helper.addReportStep(link);
        } catch (Exception e) {
            System.out.println("Something went wrong in Payment test");
            e.printStackTrace();
        }

        helper.addPropertyForReporting("nvProfile", nvProfile);
        helper.addPropertyForReporting("captureLevel", captureLevel);
    }

    @Test(priority = 4, dependsOnMethods = {"test_payment_response_time"})
    @Parameters({"nvProfile", "captureLevel"})
    public void test_logout_response_time(@Optional("4G-average") String nvProfile,
                                        @Optional("Application") String captureLevel) {
        try {
            helper.startGroupingOfSteps("Logout Flow");

            helper.startCapturePerformanceMetrics(nvProfile, captureLevel, "com.experitest.ExperiBank");

            driver.findElement(By.id("com.experitest.ExperiBank:id/logoutButton")).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.id("com.experitest.ExperiBank:id/usernameTextField")));

            String response = helper.endCapturePerformanceMetrics("Logout Flow");

            helper.endGroupingOfSteps();

            String link = helper.getPropertyFromPerformanceTransactionReport(response, "link");
            helper.addReportStep(link);
        } catch (Exception e) {
            System.out.println("Something went wrong in Logout test");
            e.printStackTrace();
        }

        helper.addPropertyForReporting("nvProfile", nvProfile);
        helper.addPropertyForReporting("captureLevel", captureLevel);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}