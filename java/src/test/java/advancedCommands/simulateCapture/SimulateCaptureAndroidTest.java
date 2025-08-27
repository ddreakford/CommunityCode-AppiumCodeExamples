package advancedCommands.simulateCapture;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import utils.EnvironmentConfig;

/**
 * The command allows users to test applications that use the camera on a mobile device.
 * The command injects an image file to the camera preview screen.
 * The command can run using file with a unique name in file repository or with a file URL.
 * Note: The application must be installed with simulate capture support.
 */
public class SimulateCaptureAndroidTest {
    
    private AndroidDriver driver = null;

    @BeforeMethod
    public void before() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setApp("cloud:com.experitest.uicatalog/.MainActivity")
                .setAppPackage("com.experitest.uicatalog")
                .setAppActivity(".MainActivity")
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", EnvironmentConfig.getAndroidDeviceQuery())
                .amend("digitalai:testName", "Run simulate capture test on Android device")
                .amend("digitalai:instrumentApp", true)
                .amend("autoGrantPermissions", true);
        driver = new AndroidDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
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
    public void simulateCaptureFromURL() throws InterruptedException {
        runSimulateCapture(true);
    }

    @Test
    public void simulateCaptureFromFileRepository() throws Exception {
        runSimulateCapture(false);
    }

    @AfterMethod
    public void tearDown() {
        
        if (driver != null) {
            driver.quit();
        }
    }
}
