package advancedCommands.simulateCapture;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

/**
 * The command allows users to test applications that use the camera on a mobile device.
 * The command injects an image file to the camera preview screen.
 * The command can run using file with a unique name in file repository or with a file URL.
 * Note: The application must be installed with simulate capture support.
 */
class SimulateCaptureAndroidTest {

    private static final String CLOUD_URL = "<CLOUD_URL>/wd/hub";
    private static final String ACCESS_KEY = "<ACCESS_KEY>";
    private static final String APPIUM_VERSION = "<APPIUM_VERSION>";

    private AndroidDriver driver = null;

    @BeforeEach
    public void before() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setAutomationName("UiAutomator2")
                .setApp("cloud:com.experitest.uicatalog/.MainActivity")
                .setAppPackage("com.experitest.uicatalog")
                .setAppActivity(".MainActivity");
        options.setCapability("accessKey", ACCESS_KEY);
        options.setCapability("appiumVersion", APPIUM_VERSION);
        options.setCapability("deviceQuery", "@os='android'");
        options.setCapability("testName", "Run simulate capture test on Android device");
        options.setCapability("autoGrantPermissions", true);
        options.setCapability("instrumentApp", true);
        driver = new AndroidDriver(new URL(CLOUD_URL), options);
    }


    private void runSimulateCapture(boolean fromURL) throws InterruptedException {
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollForward()"));
        } catch (NoSuchElementException e) {
            // ignore
        }

        driver.findElement(By.xpath("//*[@text='Camera']")).click();
        driver.findElement(AppiumBy.xpath("//*[@text='CAMERA API2 (JPEG)' or @id='scanner3']")).click();
        if (fromURL) {
            driver.executeScript("seetest:client.simulateCapture", "<FILE_URL>");
        } else {
            // File with unique name <FILE_UNIQUE_NAME> must exist in file repository
            driver.executeScript("seetest:client.simulateCapture", "cloud:<FILE_UNIQUE_NAME>");
        }
        Thread.sleep(5000); // time to wait until the image will be shown
    }


    @Test
    void simulateCaptureFromURL() throws InterruptedException {
        runSimulateCapture(true);
    }

    @Test
    void simulateCaptureFromFileRepository() throws Exception {
        runSimulateCapture(false);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }


}
