package advancedCommands.setLocation;

import io.appium.java_client.Location;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Set the current geo location
 */
class SetLocationAndroidTest {

    private static final String CLOUD_URL = "<CLOUD_URL>/wd/hub";
    private static final String ACCESS_KEY = "<ACCESS_KEY>";
    private static final String APPIUM_VERSION = "<APPIUM_VERSION>";

    private AndroidDriver driver = null;

    @BeforeEach
    public void before() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .amend("digitalai:accessKey", ACCESS_KEY)
                .amend("digitalai:appiumVersion", APPIUM_VERSION)
                .amend("digitalai:deviceQuery", "@os='android'")
                .amend("digitalai:testName", "Set location test on Android device");
        driver = new AndroidDriver(new URL(CLOUD_URL), options);
    }

    @Test
    void setLocation() {
        driver.setLocation(new Location(32, 24, 24.0));
        Location loc = driver.getLocation();

        assertEquals(32, loc.getLatitude());
        assertEquals(24, loc.getLongitude());
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }


}
