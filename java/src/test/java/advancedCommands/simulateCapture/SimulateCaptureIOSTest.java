package advancedCommands.simulateCapture;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.EnvironmentConfig;

/**
 * The command allows users to test applications that use the camera on a mobile device.
 * The command injects an image file to the camera preview screen.
 * The command can run using file with a unique name in file repository or with a file URL.
 * Note: The application must be installed with simulate capture support.
 **/
public class SimulateCaptureIOSTest {
    
    private IOSDriver driver = null;

    @BeforeMethod
    public void before() throws MalformedURLException {
        XCUITestOptions options = new XCUITestOptions()
                .setApp("cloud:com.experitest.UICatalog")
                .setBundleId("com.experitest.UICatalog")
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", EnvironmentConfig.getIOSDeviceQuery())
                .amend("digitalai:testName", "Run simulate capture test on iOS device")
                .amend("digitalai:instrumentApp", true)
                .amend("autoGrantPermissions", true);
        driver = new IOSDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
    }

    private void runSimulateCapture(boolean fromURL) throws InterruptedException {
        driver.findElement(AppiumBy.xpath("//*[@label='CameraAVTitle']")).click();

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
