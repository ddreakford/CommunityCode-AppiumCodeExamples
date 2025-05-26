package advancedCommands.installApp;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
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
class InstallAppIOSTest {


    private static final String CLOUD_URL = "<CLOUD_URL>/wd/hub";
    private static final String ACCESS_KEY = "<ACCESS_KEY>";
    private static final String APPIUM_VERSION = "<APPIUM_VERSION>";
    private static final String APP_BUILD_VERSION = "<APPLICATION_BUILD_VERSION>";
    private static final String APP_RELEASE_VERSION = "<APPLICATION_RELEASE_VERSION>";
    private static final String APP_UNIQUE_NAME = "<APPLICATION_UNIQUE_NAME>";
    private static final String APP_PACKAGE = "com.experitest.ExperiBank";

    private IOSDriver driver = null;

    @BeforeEach
    public void before() throws MalformedURLException {
        XCUITestOptions options = new XCUITestOptions()
                .setAutomationName("XCUITest")
                .setApp("digitalai:cloud:com.experitest.ExperiBank")
                .setBundleId(APP_PACKAGE)
                .amend("digitalai:accessKey", ACCESS_KEY)
                .amend("digitalai:appiumVersion", APPIUM_VERSION)
                .amend("digitalai:deviceQuery", "@os='ios'")
                .amend("digitalai:testName", "Install application test on iOS device");
        driver = new IOSDriver(new URL(CLOUD_URL), options);
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
