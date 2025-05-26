package advancedCommands.adbPushAndPullFile;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * ADB push File to device command and ADB pull File from device command.
 */
class ADBPushAndPullFileAndroidTest {

    private static final String CLOUD_URL = "<CLOUD_URL>/wd/hub";
    private static final String ACCESS_KEY = "<ACCESS_KEY>";
    private static final String APPIUM_VERSION = "<APPIUM_VERSION>";
    private static final String DEVICE_PATH = "<DEVICE_PATH>"; // e.g., "/sdcard/DCIM/Camera/hello/myPic.PNG"

    private AndroidDriver driver = null;

    @BeforeEach
    public void before() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .amend("digitalai:accessKey", ACCESS_KEY)
                .amend("digitalai:appiumVersion", APPIUM_VERSION)
                .amend("digitalai:deviceQuery", "@os='android'")
                .amend("digitalai:testName", "ADB push and pull file test on Android device");
        driver = new AndroidDriver(new URL(CLOUD_URL), options);
    }



    @Test
    void performPushFile() {
        // File with unique name <FILE_UNIQUE_NAME> must exist in file repository
        // File from repository will be uploaded to provided path on device.
        driver.executeScript("seetest:client.pushFile", DEVICE_PATH, "cloud:<FILE_UNIQUE_NAME>");
    }

    @Test
    void performPullFile() {
        // File with unique name <FILE_UNIQUE_NAME> must NOT exist in file repository
        // File downloaded from device will be stored in file repository with given unique name.
        driver.executeScript("seetest:client.pullFile", DEVICE_PATH, "cloud:<FILE_UNIQUE_NAME>");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }


}
