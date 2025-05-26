package advancedCommands.performanceTransactionForApplication;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;

/**
 * Start Performance Transaction collects data on duration, CPU, battery, and memory used by the specified application on the device.
 * Network traffic is measured for the device, and not per application.
 * You must use EndPerformanceTransaction after using this command.
 */
class PerformanceTransactionForApplicationIOSTest {

    private static final String CLOUD_URL = "<CLOUD_URL>/wd/hub";
    private static final String ACCESS_KEY = "<ACCESS_KEY>";
    private static final String APPIUM_VERSION = "<APPIUM_VERSION>";

    private IOSDriver driver = null;

    @BeforeEach
    public void before() throws MalformedURLException {
        XCUITestOptions options = new XCUITestOptions()
                .setAutomationName("XCUITest")
                .setApp("cloud:com.experitest.ExperiBank")
                .setBundleId("com.experitest.ExperiBank")
                .amend("digitalai:accessKey", ACCESS_KEY)
                .amend("digitalai:appiumVersion", APPIUM_VERSION)
                .amend("digitalai:deviceQuery", "@os='ios'")
                .amend("digitalai:instrumentApp", true)
                .amend("digitalai:testName", "Performance transaction for application test on iOS device");
        driver = new IOSDriver(new URL(CLOUD_URL), options);
    }


    @Test
    void performPerformanceTransaction() {
        driver.executeScript("seetest:client.startPerformanceTransactionForApplication", "com.experitest.ExperiBank",
                "Monitor");
        driver.rotate(ScreenOrientation.PORTRAIT);
        driver.findElement(By.xpath("//*[@name='usernameTextField']")).sendKeys("company");
        driver.findElement(By.xpath("//*[@name='passwordTextField']")).sendKeys("company");
        driver.findElement(By.xpath("//*[@name='loginButton']")).click();
        driver.executeScript("seetest:client.endPerformanceTransaction", "Transaction Test");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }


}
