package advancedCommands.setAuthenticationReply;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Simulates different authentication responses on applications that request a user fingerprint authentication.
 * This method allows setting the desired response type for later authentication requests.
 * Note: The application must be installed with fingerprint support.
 */
class SetAuthenticationReplyIOSTest {

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
        options.setCapability("instrumentApp", true);
        options.setCapability("testName", "Set authentication reply test on iOS device");
        driver = new IOSDriver(new URL(CLOUD_URL), options);
    }

    @Test
    void setAuthenticationReply() {
        final HashMap<String, String> scrollObject = new HashMap<>();
        scrollObject.put("direction", "down");
        try {
            driver.executeScript("mobile:scroll", scrollObject);
            Thread.sleep(2000); // always allow swipe action to complete
        } catch (Exception e) {
            fail("mobileScrollIOS(): FAILED\n" + e.getMessage());

        }
        driver.findElement(AppiumBy.xpath("//*[@label='Authentication']")).click();
        driver.executeScript("seetest:client.setAuthenticationReply", "Success", 1000);
        driver.findElement(AppiumBy.xpath("//*[@label='Request Touch ID Authentication']")).click();

        try {
            driver.findElement(AppiumBy.xpath("//*[@label='Success']"));
        } catch (NoSuchElementException e) {
            fail("Set authentication failed", e);
        }
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

}
