package advancedCommands.setLocation;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.Location;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriverException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Set the current geo location
 */
class SetLocationIOSTest {

    private static final String CLOUD_URL = "<CLOUD_URL>/wd/hub";
    private static final String ACCESS_KEY = "<ACCESS_KEY>";
    private static final String APPIUM_VERSION = "<APPIUM_VERSION>";

    private IOSDriver driver = null;

    @BeforeEach
    public void before() throws MalformedURLException {
        XCUITestOptions options = new XCUITestOptions()
                .setAutomationName("XCUITest");
        options.setCapability("accessKey", ACCESS_KEY);
        options.setCapability("appiumVersion", APPIUM_VERSION);
        options.setCapability("deviceQuery", "@os='ios'");
        options.setCapability("testName", "Set location test on iOS device");
        options.setCapability("bundleId", "com.apple.Preferences");
        driver = new IOSDriver(new URL(CLOUD_URL), options);
    }


    @Test
    void setLocation() {
        driver.activateApp("com.apple.Preferences");
        driver.setLocation(new Location(32, 24, 24.0));
        Location loc;

        try {
            loc = driver.getLocation();
        } catch (WebDriverException e) {
            // need to configure location permissions for ios
            if (e.getMessage().contains("Location service must be set to 'Always'")) {
                configureIosLocationPermission();
                loc = driver.getLocation();
            } else {
                throw e;
            }
        }

        assertEquals(32.0, loc.getLatitude());
        assertEquals(24.0, loc.getLongitude());
    }

    private void configureIosLocationPermission() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("direction", "down");
        configMap.put("label", "Privacy");
        driver.executeScript("mobile: scroll", configMap);
        driver.findElement(AppiumBy.xpath("//*[@label='Privacy']")).click();
        driver.findElement(AppiumBy.xpath("//*[@label='Location Services']")).click();
        driver.findElement(AppiumBy.xpath("//*[@label='WebDriverAgentRunner-Runner']")).click();
        driver.findElement(AppiumBy.xpath("//*[@label='Always']")).click();
    }


    @AfterEach
    public void tearDown() {
        driver.quit();
    }

}
