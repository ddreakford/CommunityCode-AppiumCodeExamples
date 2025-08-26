package advancedCommands.loggingDevice;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import utils.EnvironmentConfig;

/**
 * StartLoggingDevice and StopLoggingDevice commands are used to start and stop the device log. It is written to the path provided by the user.
 */
public class LoggingDeviceAndroidTest {
    
    private AndroidDriver driver = null;

    @BeforeMethod
    public void before() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setApp("cloud:com.experitest.ExperiBank/.LoginActivity")
                .setAppPackage("com.experitest.ExperiBank")
                .setAppActivity(".LoginActivity")
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", "@os='android'")
                .amend("digitalai:testName", "Logging device test on Android device");
        driver = new AndroidDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
    }

    @Test
    public void performLoggingDevice() {
        // File with unique name <FILE_UNIQUE_NAME> must not exist in file repository
        driver.executeScript("seetest:client.startLoggingDevice", "cloud:<FILE_UNIQUE_NAME>.log");
        driver.rotate(ScreenOrientation.PORTRAIT);
        driver.findElement(By.id("com.experitest.ExperiBank:id/usernameTextField")).sendKeys("company");
        driver.findElement(By.id("com.experitest.ExperiBank:id/passwordTextField")).sendKeys("company");
        driver.findElement(By.id("com.experitest.ExperiBank:id/loginButton")).click();
        driver.executeScript("seetest:client.stopLoggingDevice");
    }

    @AfterMethod
    public void tearDown() {
        
        if (driver != null) {
            driver.quit();
        }
    }

}
