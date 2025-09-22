package optionalCapabilities.installOnlyForUpdate;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import utils.ApplicationUploadUtil;
import utils.EnvironmentConfig;

/**
 * Install the application only if the desired version is different from the application version on the device (or if the app does not exist on the device).
 * Otherwise, keep the application on the device.
 * At least one of the following desired capabilities must also be specified if using this feature: appBuildVersion or appReleaseVersion.
 */
public class InstallOnlyForUpdateAndroidTest {


    private AndroidDriver driver = null;
    private ApplicationUploadUtil uploadUtil;

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        uploadUtil = new ApplicationUploadUtil();
        // Upload the unmodified version of the application
        uploadUtil.uploadUnmodifiedApplication();

        UiAutomator2Options options = new UiAutomator2Options()
                .setApp("cloud:uniqueName=" + ApplicationUploadUtil.SEETEST_UNMODIFIED)
                .setAppPackage("com.experitest.ExperiBank")
                .setAppActivity(".LoginActivity")
                .amend("digitalai:appReleaseVersion", "1.2714") // Use the desired app release version
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", EnvironmentConfig.getAndroidDeviceQuery())
                .amend("digitalai:installOnlyForUpdate", true)
                .amend("digitalai:testName", "Install application only for update test on Android device");
        driver = new AndroidDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
    }

    @Test
    public void login() {
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
