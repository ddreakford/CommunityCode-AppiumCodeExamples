package optionalCapabilities.installOnlyForUpdate;

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
 * Install the application only if the desired version is different from the application version on the device (or if the app does not exist on the device).
 * Otherwise, keep the application on the device.
 * At least one of the following desired capabilities must also be specified if using this feature: appBuildVersion or appReleaseVersion.
 */
public class InstallOnlyForUpdateIOSTest {


    private IOSDriver driver = null;

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        XCUITestOptions options = new XCUITestOptions()
                .setApp("cloud:com.experitest.ExperiBank")
                .setBundleId("com.experitest.ExperiBank")
                .amend("digitalai:appReleaseVersion", "1.0") // Use the desired app release version
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", "@os='ios'")
                .amend("digitalai:installOnlyForUpdate", true)
                .amend("digitalai:testName", "Install application only for update test on iOS device");
        driver = new IOSDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
    }

    @Test
    public void login() {
        driver.rotate(ScreenOrientation.PORTRAIT);
        driver.findElement(By.xpath("//*[@name='usernameTextField']")).sendKeys("company");
        driver.findElement(By.xpath("//*[@name='passwordTextField']")).sendKeys("company");
        driver.findElement(By.xpath("//*[@name='loginButton']")).click();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
