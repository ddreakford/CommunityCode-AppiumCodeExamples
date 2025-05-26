package advancedCommands.report;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Adds a step to the generated report
 */
class ReportIOSTest {

    private static final String CLOUD_URL = "<CLOUD_URL>/wd/hub";
    private static final String ACCESS_KEY = "<ACCESS_KEY>";
    private static final String APPIUM_VERSION = "<APPIUM_VERSION>";

    private IOSDriver driver = null;

    @BeforeEach
    public void before() throws MalformedURLException {
        XCUITestOptions options = new XCUITestOptions()
                .setApp("cloud:com.experitest.ExperiBank")
                .setBundleId("com.experitest.ExperiBank")
                .amend("digitalai:accessKey", ACCESS_KEY)
                .amend("digitalai:appiumVersion", APPIUM_VERSION)
                .amend("digitalai:deviceQuery", "@os='ios'")
                .amend("digitalai:testName", "Report test on iOS device");
        driver = new IOSDriver(new URL(CLOUD_URL), options);
    }

    @Test
    void addPassedStep() {
        driver.executeScript("seetest:client.report", "step should be passed", "true");
    }

    @Test
    void addFailedStep() {
        driver.executeScript("seetest:client.report", "step should be failed", "false");
    }


    @AfterEach
    public void tearDown() {
        driver.quit();
    }

}
