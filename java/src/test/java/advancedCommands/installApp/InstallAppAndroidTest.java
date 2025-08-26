package advancedCommands.installApp;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.EnvironmentConfig;

/**
 Install the application when given:
 * Name
 * Unique name
 * Build version or release version or both
 */
public class InstallAppAndroidTest {
    
    private static final String APP_BUILD_VERSION = "<APPLICATION_BUILD_VERSION>";
    private static final String APP_RELEASE_VERSION = "<APPLICATION_RELEASE_VERSION>";
    private static final String APP_UNIQUE_NAME = "<APPLICATION_UNIQUE_NAME>";
    private static final String APP_PACKAGE = "com.experitest.ExperiBank";

    private AndroidDriver driver = null;

    @BeforeMethod
    public void before() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setApp("cloud:com.experitest.ExperiBank/.LoginActivity")
                .setAppPackage(APP_PACKAGE)
                .setAppActivity(".LoginActivity")
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", "@os='android'")
                .amend("digitalai:testName", "Install application test on Android device");
        driver = new AndroidDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
    }

    @Test
    public void installAppByName() {
        driver.installApp("cloud:" + APP_PACKAGE);
    }

    @Test
    public void installAppByUniqueName() {
        driver.installApp("cloud:uniqueName=" + APP_UNIQUE_NAME);
    }

    @Test
    public void installAppByBuildVersion() {
        driver.installApp(
                "cloud:" + APP_PACKAGE +
                        ":buildVersion=" + APP_BUILD_VERSION);
    }

    @Test
    public void installAppByReleaseVersion() {
        driver.installApp(
                "cloud:" + APP_PACKAGE +
                        ":releaseVersion=" + APP_RELEASE_VERSION);
    }


    @Test
    public void installAppByReleaseVersionAndBuildVersion() {
        driver.installApp(
                "cloud:" + APP_PACKAGE +
                        ":releaseVersion=" + APP_RELEASE_VERSION +
                        ":buildVersion=" + APP_BUILD_VERSION);
    }

    @AfterMethod
    public void tearDown() {
        
        if (driver != null) {
            driver.quit();
        }
    }

}
