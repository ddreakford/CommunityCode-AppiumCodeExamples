package advancedCommands.loggingDevice;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;

/**
 * StartLoggingDevice and StopLoggingDevice commands are used to start and stop the device log. It is written to the path provided by the user.
 */
class LoggingDeviceAndroidTest {

    private static final String CLOUD_URL = "<CLOUD_URL>/wd/hub";
    private static final String ACCESS_KEY = "<ACCESS_KEY>";
    private static final String APPIUM_VERSION = "<APPIUM_VERSION>";

    private AndroidDriver driver = null;

    @BeforeEach
    public void before() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setAutomationName("UiAutomator2")
                .setApp("cloud:com.experitest.ExperiBank/.LoginActivity")
                .setAppPackage("com.experitest.ExperiBank")
                .setAppActivity(".LoginActivity");
        options.setCapability("accessKey", ACCESS_KEY);
        options.setCapability("appiumVersion", APPIUM_VERSION);
        options.setCapability("deviceQuery", "@os='android'");
        options.setCapability("testName", "Logging device test on Android device");
        driver = new AndroidDriver(new URL(CLOUD_URL), options);
    }

    @Test
    void performLoggingDevice() {
        // File with unique name <FILE_UNIQUE_NAME> must not exist in file repository
        driver.executeScript("seetest:client.startLoggingDevice", "cloud:<FILE_UNIQUE_NAME>.log");
        driver.rotate(ScreenOrientation.PORTRAIT);
        driver.findElement(By.id("com.experitest.ExperiBank:id/usernameTextField")).sendKeys("company");
        driver.findElement(By.id("com.experitest.ExperiBank:id/passwordTextField")).sendKeys("company");
        driver.findElement(By.id("com.experitest.ExperiBank:id/loginButton")).click();
        driver.executeScript("seetest:client.stopLoggingDevice");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

}
