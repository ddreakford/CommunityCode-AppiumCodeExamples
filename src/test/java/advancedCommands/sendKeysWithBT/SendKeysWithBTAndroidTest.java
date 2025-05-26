package advancedCommands.sendKeysWithBT;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

/**
 * This command sends keyboard events to the Bluetooth keyboard on the device.
 */
class SendKeysWithBTAndroidTest {

    private static final String CLOUD_URL = "<CLOUD_URL>/wd/hub";
    private static final String ACCESS_KEY = "<ACCESS_KEY>";
    private static final String APPIUM_VERSION = "<APPIUM_VERSION>";

    private AndroidDriver driver = null;

    @BeforeEach
    public void before() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setAutomationName("UiAutomator2")
                .setApp("cloud:com.experitest.ExperiBank/.LoginActivity")
                .setAppPackage("com.experitest.ExperiBank")
                .setAppActivity(".LoginActivity")
                .amend("digitalai:accessKey", ACCESS_KEY)
                .amend("digitalai:appiumVersion", APPIUM_VERSION)
                .amend("digitalai:deviceQuery", "@os='android'")
                .amend("digitalai:instrumentApp", true)
                .amend("digitalai:testName", "Send key with BT test on Android device");
        driver = new AndroidDriver(new URL(CLOUD_URL), options);
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
