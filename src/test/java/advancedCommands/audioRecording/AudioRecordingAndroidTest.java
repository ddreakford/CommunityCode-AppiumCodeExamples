package advancedCommands.audioRecording;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Start recording to a file from the device.
 */
class AudioRecordingAndroidTest {

    private static final String CLOUD_URL = "<CLOUD_URL>/wd/hub";
    private static final String ACCESS_KEY = "<ACCESS_KEY>";
    private static final String APPIUM_VERSION = "<APPIUM_VERSION>";

    private AndroidDriver driver = null;

    @BeforeEach
    public void before() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setAutomationName("UiAutomator2");
        options.setCapability("accessKey", ACCESS_KEY);
        options.setCapability("appiumVersion", APPIUM_VERSION);
        options.setCapability("deviceQuery", "@os='android'");
        options.setCapability("instrumentApp", true);
        options.setCapability("testName", "Performance audio recording test on Android device");
        driver = new AndroidDriver(new URL(CLOUD_URL), options);
    }

    @Test
    void performAudioRecording() throws InterruptedException {
        // File with unique name <FILE_UNIQUE_NAME> must not exist in file repository
        driver.executeScript("seetest:client.startAudioRecording", "cloud:<FILE_UNIQUE_NAME>");
        // commands that play audio on. For example, open youtube application and play a video
        Thread.sleep(10000);
        driver.executeScript("seetest:client.stopAudioRecording");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }


}
