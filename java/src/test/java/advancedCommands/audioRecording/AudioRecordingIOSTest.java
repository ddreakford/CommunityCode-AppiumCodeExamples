package advancedCommands.audioRecording;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.EnvironmentConfig;

/**
 * Start recording to a file from the device.
 */
public class AudioRecordingIOSTest {
    
    private IOSDriver driver = null;

    @BeforeMethod
    public void before() throws MalformedURLException {
        XCUITestOptions options = new XCUITestOptions()
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", EnvironmentConfig.getIOSDeviceQuery())
                .amend("digitalai:instrumentApp", true)
                .amend("digitalai:testName", "Performance audio recording test on iOS device");
        driver = new IOSDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
    }

    @Test
    public void performAudioRecording() throws InterruptedException {
        // File with unique name <FILE_UNIQUE_NAME> must not exist in file repository
        driver.executeScript("seetest:client.startAudioRecording", "cloud:<FILE_UNIQUE_NAME>");
        // commands that play audio on. For example, open youtube application and play a video
        Thread.sleep(10000);
        driver.executeScript("seetest:client.stopAudioRecording");
    }

    @AfterMethod
    public void tearDown() {
        
        if (driver != null) {
            driver.quit();
        }
    }


}
