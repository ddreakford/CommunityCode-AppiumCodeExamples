package advancedCommands.loggingDevice;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.ScreenOrientation;
import utils.EnvironmentConfig;

/**
 * StartLoggingDevice and StopLoggingDevice commands are used to start and stop device log will be written to the path provided by the user.
 */
public class LoggingDeviceIOSTest {
    
    private IOSDriver driver = null;

    @BeforeMethod
    public void before() throws MalformedURLException {
        XCUITestOptions options = new XCUITestOptions()
                .setApp("cloud:com.experitest.ExperiBank")
                .setBundleId("com.experitest.ExperiBank")
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", EnvironmentConfig.getIOSDeviceQuery())
                .amend("digitalai:testName", "Logging device test on iOS device");
        driver = new IOSDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
    }

    @Test
    public void performLoggingDevice() {
        // File with unique name <FILE_UNIQUE_NAME> must not exist in file repository
        driver.executeScript("seetest:client.startLoggingDevice", "cloud:<FILE_UNIQUE_NAME>.log");
        driver.rotate(ScreenOrientation.PORTRAIT);
        driver.findElement(AppiumBy.xpath("//*[@name='usernameTextField']")).sendKeys("company");
        driver.findElement(AppiumBy.xpath("//*[@name='passwordTextField']")).sendKeys("company");
        driver.findElement(AppiumBy.xpath("//*[@name='loginButton']")).click();
        driver.executeScript("seetest:client.stopLoggingDevice");
    }

    @AfterMethod
    public void tearDown() {
        
        if (driver != null) {
            driver.quit();
        }
    }

}
