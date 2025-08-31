package advancedCommands.setLocation;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.Location;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriverException;

import static org.testng.Assert.assertEquals;
import utils.EnvironmentConfig;

/**
 * Set the current geo location
 */
public class SetLocationIOSTest {
    
    private IOSDriver driver = null;

    @BeforeMethod
    public void before() throws MalformedURLException {
        XCUITestOptions options = new XCUITestOptions()
                .setBundleId("com.apple.Preferences")
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", EnvironmentConfig.getIOSDeviceQuery())
                .amend("digitalai:testName", "Set location test on iOS device");
        driver = new IOSDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
    }

    @Test
    public void setLocation() {
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


    @AfterMethod
    public void tearDown() {
        
        if (driver != null) {
            driver.quit();
        }
    }

}
