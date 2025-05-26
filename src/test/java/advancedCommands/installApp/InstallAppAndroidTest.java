package advancedCommands.installApp;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 Install the application when given:
 * Name
 * Unique name
 * Build version or release version or both
 */
class InstallAppAndroidTest {

    private static final String CLOUD_URL = "<CLOUD_URL>/wd/hub";
    private static final String ACCESS_KEY = "<ACCESS_KEY>";
    private static final String APPIUM_VERSION = "<APPIUM_VERSION>";
    private static final String APP_BUILD_VERSION = "<APPLICATION_BUILD_VERSION>";
    private static final String APP_RELEASE_VERSION = "<APPLICATION_RELEASE_VERSION>";
    private static final String APP_UNIQUE_NAME = "<APPLICATION_UNIQUE_NAME>";
    private static final String APP_PACKAGE = "com.experitest.ExperiBank";

    private AndroidDriver driver = null;

    @BeforeEach
    public void before() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setApp("cloud:com.experitest.ExperiBank/.LoginActivity")
                .setAppPackage(APP_PACKAGE)
                .setAppActivity(".LoginActivity")
                .amend("digitalai:accessKey", ACCESS_KEY)
                .amend("digitalai:appiumVersion", APPIUM_VERSION)
                .amend("digitalai:deviceQuery", "@os='android'")
                .amend("digitalai:testName", "Install application test on Android device");
        driver = new AndroidDriver(new URL(CLOUD_URL), options);
    }

    @Test
    void installAppByName() {
        driver.installApp("cloud:" + APP_PACKAGE);
    }

    @Test
    void installAppByUniqueName() {
        driver.installApp("cloud:uniqueName=" + APP_UNIQUE_NAME);
    }

    @Test
    void installAppByBuildVersion() {
        driver.installApp(
                "cloud:" + APP_PACKAGE +
                        ":buildVersion=" + APP_BUILD_VERSION);
    }

    @Test
    void installAppByReleaseVersion() {
        driver.installApp(
                "cloud:" + APP_PACKAGE +
                        ":releaseVersion=" + APP_RELEASE_VERSION);
    }


    @Test
    void installAppByReleaseVersionAndBuildVersion() {
        driver.installApp(
                "cloud:" + APP_PACKAGE +
                        ":releaseVersion=" + APP_RELEASE_VERSION +
                        ":buildVersion=" + APP_BUILD_VERSION);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

}
