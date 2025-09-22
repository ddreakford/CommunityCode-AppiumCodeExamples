package advancedCommands.report;

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
 * Adds a step to the generated report
 */
public class ReportAndroidTest {
    
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
                .amend("digitalai:testName", "Report test on Android device");

        driver = new AndroidDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
    }

    @Test
    public void addPassedStep() {
        driver.executeScript("seetest:client.report", "step should be passed", "true");
    }

    @Test
    public void addFailedStep() {
        driver.executeScript("seetest:client.report", "step should be failed", "false");
    }


    @AfterMethod
    public void tearDown() {
        
        if (driver != null) {
            driver.quit();
        }
    }

}
