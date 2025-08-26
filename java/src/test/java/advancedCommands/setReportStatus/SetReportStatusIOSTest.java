package advancedCommands.setReportStatus;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.EnvironmentConfig;

/**
 * Overrides the final report status
 */
public class SetReportStatusIOSTest {
    
    private IOSDriver driver = null;

    @BeforeMethod
    public void before() throws MalformedURLException {
        XCUITestOptions options = new XCUITestOptions()
                .setApp("cloud:com.experitest.ExperiBank")
                .setBundleId("com.experitest.ExperiBank")
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", "@os='ios'")
                .amend("digitalai:testName", "Set Report status test on iOS device");
        driver = new IOSDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
    }

    @Test
    public void setPassed() {
        driver.executeScript("seetest:client.setReportStatus", "Passed", "passed-message");
    }

    @Test
    public void setFailed() {
        driver.executeScript("seetest:client.setReportStatus", "Failed", "failed-message",
                "failure-stacktrace");
    }

    @Test
    public void setSkipped() {
        driver.executeScript("seetest:client.setReportStatus", "Skipped", "skipped-message");
    }

    @AfterMethod
    public void tearDown() {
        
        if (driver != null) {
            driver.quit();
        }
    }

}
