package advancedCommands.setAuthenticationReply;

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
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Simulates different authentication responses on applications that request a user fingerprint authentication.
 * This method allows setting the desired response type for later authentication requests.
 * Note: The application must be installed with fingerprint support.
 */
class SetAuthenticationReplyAndroidTest {

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
        options.setCapability("instrumentApp", true);
        options.setCapability("testName", "Set authentication reply test on Android device");
        driver = new AndroidDriver(new URL(CLOUD_URL), options);
    }

    @Test
    void setAuthenticationReply() {
        try {
            driver.findElement(AppiumBy.androidUIAutomator((
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollForward()")));
        } catch (NoSuchElementException e) {
            // ignore
        }
        driver.findElement(AppiumBy.xpath("//*[@text='Fingerprint Authentication']")).click();
        driver.executeScript("seetest:client.setAuthenticationReply", "AUTHENTICATION_SUCCEEDED", "10000");
        driver.findElement(AppiumBy.xpath("//*[@text='Symmetric Authentication']")).click();
        driver.findElement(AppiumBy.xpath("//*[@text='Start']")).click();
        WebElement element = driver.findElement(AppiumBy.xpath("//*[@id='fingerprint_status']"));
        boolean fingerprintRecognizedIsDisplay = element.getText().contains("Fingerprint recognized");
        assertTrue(fingerprintRecognizedIsDisplay);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

}
