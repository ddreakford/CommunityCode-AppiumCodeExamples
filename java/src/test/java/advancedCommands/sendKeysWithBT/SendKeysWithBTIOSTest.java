package advancedCommands.sendKeysWithBT;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.Keys;
import utils.ApplicationUploadUtil;
import utils.EnvironmentConfig;

/**
 * This command sends keyboard events to the Bluetooth keyboard on the device.
 */
public class SendKeysWithBTIOSTest {
    
    private IOSDriver driver = null;
    private ApplicationUploadUtil uploadUtil;

    @BeforeMethod
    public void before() throws MalformedURLException {
        uploadUtil = new ApplicationUploadUtil();
        // Upload the unmodified version of the application
        uploadUtil.uploadUnmodifiedApplication();

        XCUITestOptions options = new XCUITestOptions()
                .setApp(ApplicationUploadUtil.SEETEST_FULLNAME)
                .setBundleId("com.experitest.ExperiBank")
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", EnvironmentConfig.getIOSDeviceQuery())
                .amend("digitalai:instrumentApp", true)
                .amend("digitalai:testName", "Send key with BT test on iOS device");
        driver = new IOSDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
    }

    @Test
    public void sendKeyWithBT() {
        driver.executeScript("seetest:client.sendKeysWithBT", "" + Keys.CONTROL + Keys.ALT + "I");
    }

    @AfterMethod
    public void tearDown() {
        
        if (driver != null) {
            driver.quit();
        }
    }

}
