package advancedCommands.audioPlay;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.EnvironmentConfig;

/**
 * Start playing an audio file.
 */
public class AudioPlayIOSTest {
    
    private IOSDriver driver = null;

    @BeforeMethod
    public void before() throws MalformedURLException {
        XCUITestOptions options = new XCUITestOptions()
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", "@os='ios'")
                .amend("digitalai:instrumentApp", true)
                .amend("digitalai:testName", "Performance audio play test on iOS device");
        driver = new IOSDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
    }

    @Test
    public void performAudioPlaying() throws InterruptedException {
        // add commands that open the voice application in the device
        // File with unique name <FILE_UNIQUE_NAME> must exist in file repository
        driver.executeScript("seetest:client.startAudioPlay", "cloud:<FILE_UNIQUE_NAME>");
        Thread.sleep(10000);
        driver.executeScript("seetest:client.stopAudioPlay");
    }

    @AfterMethod
    public void tearDown() {
        
        if (driver != null) {
            driver.quit();
        }
    }

}
