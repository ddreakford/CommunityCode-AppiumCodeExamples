package advancedCommands.adbPushAndPullFile;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.EnvironmentConfig;


/**
 * ADB push File to device command and ADB pull File from device command.
 */
public class ADBPushAndPullFileAndroidTest {
    
    private static final String DEVICE_PATH = "<DEVICE_PATH>"; // e.g., "/sdcard/DCIM/Camera/hello/myPic.PNG"

    private AndroidDriver driver = null;

    @BeforeMethod
    public void before() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", EnvironmentConfig.getAndroidDeviceQuery())
                .amend("digitalai:testName", "ADB push and pull file test on Android device");
        driver = new AndroidDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
    }



    @Test
    public void performPushFile() {
        // File with unique name <FILE_UNIQUE_NAME> must exist in file repository
        // File from repository will be uploaded to provided path on device.
        driver.executeScript("seetest:client.pushFile", DEVICE_PATH, "cloud:<FILE_UNIQUE_NAME>");
    }

    @Test
    public void performPullFile() {
        // File with unique name <FILE_UNIQUE_NAME> must NOT exist in file repository
        // File downloaded from device will be stored in file repository with given unique name.
        driver.executeScript("seetest:client.pullFile", DEVICE_PATH, "cloud:<FILE_UNIQUE_NAME>");
    }

    @AfterMethod
    public void tearDown() {
        
        if (driver != null) {
            driver.quit();
        }
    }


}
