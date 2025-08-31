package advancedCommands.setAuthenticationReply;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.NoSuchElementException;

import static org.testng.Assert.fail;
import utils.EnvironmentConfig;

/**
 * Simulates different authentication responses on applications that request a user fingerprint authentication.
 * This method allows setting the desired response type for later authentication requests.
 * Note: The application must be installed with fingerprint support.
 */
public class SetAuthenticationReplyIOSTest {
    
    private IOSDriver driver = null;

    @BeforeMethod
    public void before() throws MalformedURLException {
        XCUITestOptions options = new XCUITestOptions()
                .setApp("cloud:com.experitest.UICatalog")
                .setBundleId("com.experitest.UICatalog")
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", EnvironmentConfig.getIOSDeviceQuery())
                .amend("digitalai:instrumentApp", true)
                .amend("digitalai:testName", "Set authentication reply test on iOS device");
        driver = new IOSDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
    }

    @Test
    public void setAuthenticationReply() {
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

    @AfterMethod
    public void tearDown() {
        
        if (driver != null) {
            driver.quit();
        }
    }

}
