package optionalCapabilities.doNotGoHomeOnQuit;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;

/**
 * The device remains in the last left state even after ending the test
 * when dontGoHomeOnQuit capability is set to true.
 */
class DoNotGoHomeOnQuitIOSTest {

    private static final String CLOUD_URL = "<CLOUD_URL>/wd/hub";
    private static final String ACCESS_KEY = "<ACCESS_KEY>";
    private static final String APPIUM_VERSION = "<APPIUM_VERSION>";

    private IOSDriver driver = null;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        XCUITestOptions options = new XCUITestOptions()
                .setAutomationName("XCUITest")
                .setApp("cloud:com.experitest.ExperiBank")
                .setBundleId("com.experitest.ExperiBank");
        options.setCapability("accessKey", ACCESS_KEY);
        options.setCapability("appiumVersion", APPIUM_VERSION);
        options.setCapability("deviceQuery", "@os='ios'");
        options.setCapability("dontGoHomeOnQuit", true);
        options.setCapability("testName", "DontGoHomeOnQuit test on iOS device");
        driver = new IOSDriver(new URL(CLOUD_URL), options);
    }

    @Test
    void runTest() {
        driver.rotate(ScreenOrientation.PORTRAIT);
        driver.findElement(By.xpath("//*[@name='usernameTextField']")).sendKeys("company");
        driver.findElement(By.xpath("//*[@name='passwordTextField']")).sendKeys("company");
        driver.findElement(By.xpath("//*[@name='loginButton']")).click();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
