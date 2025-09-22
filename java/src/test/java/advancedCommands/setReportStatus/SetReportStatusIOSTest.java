package advancedCommands.setReportStatus;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ApplicationUploadUtil;
import utils.EnvironmentConfig;

/**
 * Overrides the final report status
 */
public class SetReportStatusIOSTest {
    
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
