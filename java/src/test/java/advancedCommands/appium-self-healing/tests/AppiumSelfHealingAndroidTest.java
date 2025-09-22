package advancedCommands.appium_self_healing.tests;

import advancedCommands.appium_self_healing.helpers.SelfHealingHelpers;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.EnvironmentConfig;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static org.testng.Assert.assertTrue;

public class AppiumSelfHealingAndroidTest {

    protected AndroidDriver driver;
    protected WebDriverWait wait;
    protected SelfHealingHelpers helper;

    @BeforeMethod
    @Parameters({"application.name"})
    public void setUp(@Optional ITestContext context, @Optional Method method, String applicationName) throws MalformedURLException {

        helper = new SelfHealingHelpers();
        String deviceQuery = context.getSuite().getParameter("device.query");

        // Upload version of the app based on the value of `application.name`
        System.out.println(String.format("setUp(): applicationName: %s", applicationName));

        if (applicationName.equalsIgnoreCase("unmodified")) {
            System.out.println(String.format("Uploading APK for unmodified: %s", EnvironmentConfig.getUnmodifiedBuildUrl()));
            helper.uploadApplicationApi(EnvironmentConfig.getUnmodifiedBuildUrl(), "seetest-unmodified");
        } else if (applicationName.equalsIgnoreCase("modified")) {
            System.out.println(String.format("Uploading APK for modified: %s", EnvironmentConfig.getModifiedBuildUrl()));
            helper.uploadApplicationApi(EnvironmentConfig.getModifiedBuildUrl(), "seetest-modified");
        }

        String uniqueName = applicationName.equalsIgnoreCase("unmodified") ? "seetest-unmodified" : "seetest-modified";
        UiAutomator2Options options = new UiAutomator2Options()
                .setApp("cloud:uniqueName=" + uniqueName)
                .setAppPackage("com.experitest.ExperiBank")
                .setAppActivity(".LoginActivity")
                .setFullReset(true)
                .setAutoGrantPermissions(true)
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", deviceQuery)
                .amend("digitalai:testName", method.getName())
                .amend("digitalai:selfHealing", true);

        driver = new AndroidDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        System.out.println("setUp() - Initializing the Session");
    }

    @Test
    public void login_scenario_test() throws InterruptedException {
        System.out.println("login_scenario_test() - Starting the Test");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("com.experitest.ExperiBank:id/usernameTextField")));
        driver.findElement(By.id("com.experitest.ExperiBank:id/usernameTextField")).sendKeys("company");
        driver.findElement(By.id("com.experitest.ExperiBank:id/passwordTextField")).sendKeys("company");
        driver.findElement(By.id("com.experitest.ExperiBank:id/loginButton")).click();

        Thread.sleep(3000);

        Boolean isMakePaymentButtonPresent = driver.findElement(By.id("com.experitest.ExperiBank:id/makePaymentButton")).isDisplayed();
        assertTrue(isMakePaymentButtonPresent);
        System.out.println("login_scenario_test() - Ending the Test");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        System.out.println("tearDown() - Closing the Session");
        try {
            if (result.isSuccess()) {
                driver.executeScript("seetest:client.setReportStatus", "Passed", "Test Passed");
            } else {
                driver.executeScript("seetest:client.setReportStatus", "Failed", "Test Failed");
            }
        } catch (Exception e) {}

        if (driver != null) {
            driver.quit();
        }
    }

}