package advancedCommands.audioPlay;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.EnvironmentConfig;

/**
 * Start playing an audio file.
 */
public class AudioPlayAndroidTest {
    
    private AndroidDriver driver = null;

    @BeforeMethod
    public void before() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", EnvironmentConfig.getAndroidDeviceQuery())
                .amend("digitalai:instrumentApp", true)
                .amend("digitalai:testName", "Performance audio play test on Android device");
        driver = new AndroidDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
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
