package advancedCommands.sendKeysWithBT;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

/**
 * This command sends keyboard events to the Bluetooth keyboard on the device.
 */
class SendKeysWithBTIOSTest {

    private static final String CLOUD_URL = "<CLOUD_URL>/wd/hub";
    private static final String ACCESS_KEY = "<ACCESS_KEY>";
    private static final String APPIUM_VERSION = "<APPIUM_VERSION>";

    private IOSDriver driver = null;

    @BeforeEach
    public void before() throws MalformedURLException {
        XCUITestOptions options = new XCUITestOptions()
                .setAutomationName("XCUITest")
                .setApp("cloud:com.experitest.ExperiBank")
                .setBundleId("com.experitest.ExperiBank")
                .amend("digitalai:accessKey", ACCESS_KEY)
                .amend("digitalai:appiumVersion", APPIUM_VERSION)
                .amend("digitalai:deviceQuery", "@os='ios'")
                .amend("digitalai:instrumentApp", true)
                .amend("digitalai:testName", "Send key with BT test on iOS device");
        driver = new IOSDriver(new URL(CLOUD_URL), options);
    }

    @Test
    void sendKeyWithBT() {
        driver.executeScript("seetest:client.sendKeysWithBT", "" + Keys.CONTROL + Keys.ALT + "I");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

}
