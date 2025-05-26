package advancedCommands.audioPlay;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Start playing an audio file.
 */
class AudioPlayAndroidTest {

    private static final String CLOUD_URL = "<CLOUD_URL>/wd/hub";
    private static final String ACCESS_KEY = "<ACCESS_KEY>";
    private static final String APPIUM_VERSION = "<APPIUM_VERSION>";

    private AndroidDriver driver = null;

    @BeforeEach
    public void before() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setAutomationName("UiAutomator2")
                .amend("digitalai:accessKey", ACCESS_KEY)
                .amend("digitalai:appiumVersion", APPIUM_VERSION)
                .amend("digitalai:deviceQuery", "@os='android'")
                .amend("digitalai:instrumentApp", true)
                .amend("digitalai:testName", "Performance audio play test on Android device");
        driver = new AndroidDriver(new URL(CLOUD_URL), options);
    }

    @Test
    void performAudioPlaying() throws InterruptedException {
        // add commands that open the voice application in the device
        // File with unique name <FILE_UNIQUE_NAME> must exist in file repository
        driver.executeScript("seetest:client.startAudioPlay", "cloud:<FILE_UNIQUE_NAME>");
        Thread.sleep(10000);
        driver.executeScript("seetest:client.stopAudioPlay");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

}
