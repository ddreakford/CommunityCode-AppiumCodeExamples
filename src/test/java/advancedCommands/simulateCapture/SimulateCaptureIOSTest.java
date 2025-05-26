package advancedCommands.simulateCapture;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

/**
 * The command allows users to test applications that use the camera on a mobile device.
 * The command injects an image file to the camera preview screen.
 * The command can run using file with a unique name in file repository or with a file URL.
 * Note: The application must be installed with simulate capture support.
 **/
class SimulateCaptureIOSTest {

    private static final String CLOUD_URL = "<CLOUD_URL>/wd/hub";
    private static final String ACCESS_KEY = "<ACCESS_KEY>";
    private static final String APPIUM_VERSION = "<APPIUM_VERSION>";

    private IOSDriver driver = null;

    @BeforeEach
    public void before() throws MalformedURLException {
        XCUITestOptions options = new XCUITestOptions()
                .setAutomationName("XCUITest")
                .setApp("cloud:com.experitest.UICatalog")
                .setBundleId("com.experitest.UICatalog");
        options.setCapability("accessKey", ACCESS_KEY);
        options.setCapability("appiumVersion", APPIUM_VERSION);
        options.setCapability("deviceQuery", "@os='ios'");
        options.setCapability("testName", "Run simulate capture test on iOS device");
        options.setCapability("autoGrantPermissions", true);
        options.setCapability("instrumentApp", true);
        driver = new IOSDriver(new URL(CLOUD_URL), options);
    }

    private void runSimulateCapture(boolean fromURL) throws InterruptedException {
        driver.findElement(By.xpath("//*[@text='CameraAVTitle']")).click();

        Thread.sleep(10000); // time to wait until the image will be shown
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
