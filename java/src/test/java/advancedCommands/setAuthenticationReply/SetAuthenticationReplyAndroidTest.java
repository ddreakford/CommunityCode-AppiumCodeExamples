package advancedCommands.setAuthenticationReply;

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
import org.openqa.selenium.WebElement;

import static org.testng.Assert.assertTrue;
import utils.EnvironmentConfig;

/**
 * Simulates different authentication responses on applications that request a user fingerprint authentication.
 * This method allows setting the desired response type for later authentication requests.
 * Note: The application must be installed with fingerprint support.
 */
public class SetAuthenticationReplyAndroidTest {
    
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
                .amend("digitalai:instrumentApp", true)
                .amend("digitalai:testName", "Set authentication reply test on Android device");
        driver = new AndroidDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
    }

    @Test
    public void setAuthenticationReply() {
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollForward()"));
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

    @AfterMethod
    public void tearDown() {
        
        if (driver != null) {
            driver.quit();
        }
    }

}
