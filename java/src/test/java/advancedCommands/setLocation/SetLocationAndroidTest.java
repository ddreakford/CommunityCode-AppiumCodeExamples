package advancedCommands.setLocation;

import io.appium.java_client.Location;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import utils.EnvironmentConfig;

/**
 * Set the current geo location
 */
public class SetLocationAndroidTest {
    
    private AndroidDriver driver = null;

    @BeforeMethod
    public void before() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", "@os='android'")
                .amend("digitalai:testName", "Set location test on Android device");
        driver = new AndroidDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
    }

    @Test
    public void setLocation() {
        driver.setLocation(new Location(32, 24, 24.0));
        Location loc = driver.getLocation();

        assertEquals(32, loc.getLatitude());
        assertEquals(24, loc.getLongitude());
    }

    @AfterMethod
    public void tearDown() {
        
        if (driver != null) {
            driver.quit();
        }
    }


}
