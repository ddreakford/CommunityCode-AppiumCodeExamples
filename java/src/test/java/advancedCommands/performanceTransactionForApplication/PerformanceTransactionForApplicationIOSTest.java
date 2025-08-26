package advancedCommands.performanceTransactionForApplication;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import utils.EnvironmentConfig;

/**
 * Start Performance Transaction collects data on duration, CPU, battery, and memory used by the specified application on the device.
 * Network traffic is measured for the device, and not per application.
 * You must use EndPerformanceTransaction after using this command.
 */
public class PerformanceTransactionForApplicationIOSTest {
    
    private IOSDriver driver = null;

    @BeforeMethod
    public void before() throws MalformedURLException {
        XCUITestOptions options = new XCUITestOptions()
                .setApp("cloud:com.experitest.ExperiBank")
                .setBundleId("com.experitest.ExperiBank")
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", "@os='ios'")
                .amend("digitalai:instrumentApp", true)
                .amend("digitalai:testName", "Performance transaction for application test on iOS device");
        driver = new IOSDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
    }


    @Test
    public void performPerformanceTransaction() {
        driver.executeScript("seetest:client.startPerformanceTransactionForApplication", "com.experitest.ExperiBank",
                "Monitor");
        driver.rotate(ScreenOrientation.PORTRAIT);
        driver.findElement(By.xpath("//*[@name='usernameTextField']")).sendKeys("company");
        driver.findElement(By.xpath("//*[@name='passwordTextField']")).sendKeys("company");
        driver.findElement(By.xpath("//*[@name='loginButton']")).click();
        driver.executeScript("seetest:client.endPerformanceTransaction", "Transaction Test");
    }

    @AfterMethod
    public void tearDown() {
        
        if (driver != null) {
            driver.quit();
        }
    }


}
