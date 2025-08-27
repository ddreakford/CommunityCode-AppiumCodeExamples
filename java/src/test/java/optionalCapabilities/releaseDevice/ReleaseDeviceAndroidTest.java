package optionalCapabilities.releaseDevice;

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
 * Gives the capability to not release a device after performing driver.quit();
 * For that, set the capability 'releaseDevice' to false
 */
public class ReleaseDeviceAndroidTest {
    
    private AndroidDriver driver = null;

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        driver = new AndroidDriver(new URL(EnvironmentConfig.getCloudUrl()), new UiAutomator2Options()
                .setApp("cloud:com.experitest.ExperiBank/.LoginActivity")
                .setAppPackage("com.experitest.ExperiBank")
                .setAppActivity(".LoginActivity")
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", EnvironmentConfig.getAndroidDeviceQuery())
                .amend("digitalai:releaseDevice", false)
                .amend("digitalai:testName", "Release device test on Android device")
        );
    }

    @Test
    public void runTestWithReleaseDeviceFalse() {
        driver.rotate(ScreenOrientation.PORTRAIT);
        driver.findElement(By.id("com.experitest.ExperiBank:id/usernameTextField")).sendKeys("company");
        driver.findElement(By.id("com.experitest.ExperiBank:id/passwordTextField")).sendKeys("company");
        driver.findElement(By.id("com.experitest.ExperiBank:id/loginButton")).click();
    }

    @AfterMethod
    public void tearDown() {
        
        if (driver != null) {
            driver.quit();
        }
    }
}
