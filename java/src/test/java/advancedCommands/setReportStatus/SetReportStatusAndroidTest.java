package advancedCommands.setReportStatus;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
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
public class SetReportStatusAndroidTest {
    
    private AndroidDriver driver = null;
    private ApplicationUploadUtil uploadUtil;

    @BeforeMethod
    public void before() throws MalformedURLException {
        uploadUtil = new ApplicationUploadUtil();
        // Upload the unmodified version of the application
        uploadUtil.uploadUnmodifiedApplication();

        UiAutomator2Options options = new UiAutomator2Options()
                .setApp("cloud:uniqueName=" + ApplicationUploadUtil.SEETEST_UNMODIFIED)
                .setAppPackage("com.experitest.ExperiBank")
                .setAppActivity(".LoginActivity")
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", EnvironmentConfig.getAndroidDeviceQuery())
                .amend("digitalai:testName", "Set Report status test on Android device");
        driver = new AndroidDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
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
