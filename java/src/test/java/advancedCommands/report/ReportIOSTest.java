package advancedCommands.report;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.EnvironmentConfig;

/**
 * Adds a step to the generated report
 */
public class ReportIOSTest {
    
    private IOSDriver driver = null;

    @BeforeMethod
    public void before() throws MalformedURLException {
        XCUITestOptions options = new XCUITestOptions()
                .setApp("cloud:com.experitest.ExperiBank")
                .setBundleId("com.experitest.ExperiBank")
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", "@os='ios'")
                .amend("digitalai:testName", "Report test on iOS device");
        driver = new IOSDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
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
