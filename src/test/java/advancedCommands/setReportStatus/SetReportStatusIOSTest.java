package advancedCommands.setReportStatus;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Overrides the final report status
 */
class SetReportStatusIOSTest {

    private static final String CLOUD_URL = "<CLOUD_URL>/wd/hub";
    private static final String ACCESS_KEY = "<ACCESS_KEY>";
    private static final String APPIUM_VERSION = "<APPIUM_VERSION>";

    private IOSDriver driver = null;

    @BeforeEach
    public void before() throws MalformedURLException {
        XCUITestOptions options = new XCUITestOptions()
                .setAutomationName("XCUITest")
                .setApp("cloud:com.experitest.ExperiBank")
                .setBundleId("com.experitest.ExperiBank");
        options.setCapability("accessKey", ACCESS_KEY);
        options.setCapability("appiumVersion", APPIUM_VERSION);
        options.setCapability("deviceQuery", "@os='ios'");
        options.setCapability("testName", "Set Report status test on iOS device");
        driver = new IOSDriver(new URL(CLOUD_URL), options);
    }

    @Test
    void setPassed() {
        driver.executeScript("seetest:client.setReportStatus", "Passed", "passed-message");
    }

    @Test
    void setFailed() {
        driver.executeScript("seetest:client.setReportStatus", "Failed", "failed-message",
                "failure-stacktrace");
    }

    @Test
    void setSkipped() {
        driver.executeScript("seetest:client.setReportStatus", "Skipped", "skipped-message");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

}
