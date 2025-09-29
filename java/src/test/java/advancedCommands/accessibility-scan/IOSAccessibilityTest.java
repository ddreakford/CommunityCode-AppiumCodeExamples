package advancedCommands.accessibilityscan;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.AccessibilityResultsUtil;
import utils.ApplicationUploadUtil;
import utils.EnvironmentConfig;

public class IOSAccessibilityTest {

    private IOSDriver driver = null;
    private ApplicationUploadUtil uploadUtil;

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        uploadUtil = new ApplicationUploadUtil();
        uploadUtil.uploadUnmodifiedApplication();

        XCUITestOptions options = new XCUITestOptions()
                .setApp(ApplicationUploadUtil.SEETEST_FULLNAME)
                .setBundleId("com.experitest.ExperiBank")
                .amend("digitalai:testName", "Run Accessibility Scan test on iOS device")
                .amend("digitalai:accessKey", EnvironmentConfig.getAccessKey())
                .amend("digitalai:appiumVersion", EnvironmentConfig.getAppiumVersion())
                .amend("digitalai:deviceQuery", EnvironmentConfig.getIOSDeviceQuery());

        driver = new IOSDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
    }

    @Test
    public void accessibilityScan() {
        try {
            Map<String, Object> settings = new HashMap<>();
            settings.put("apiKey", EnvironmentConfig.getAxeDevToolsApiKey());
            settings.put("scanName", "iOS_ExperiBank_Accessibility_Scan");

            System.out.println("Starting accessibility scan on iOS device...");

            Object scanResults = driver.executeScript("mobile: axeScan", settings);

            if (scanResults != null) {
                System.out.println("Accessibility scan completed successfully.");
                AccessibilityResultsUtil.saveResults(scanResults, "iOS", "iOS_ExperiBank_Accessibility_Scan");
                System.out.println("Scan results have been saved to reports/accessibility/");
            } else {
                Assert.fail("Accessibility scan returned null results. Check device configuration and axe DevTools integration.");
            }

        } catch (Exception e) {
            String errorMessage = String.format(
                "Accessibility scan failed on iOS device. Error: %s. " +
                "Please verify: 1) AXE_DEVTOOLS_API_KEY is configured in .env file, " +
                "2) Deque axe DevTools for Mobile is properly integrated, " +
                "3) Device supports accessibility scanning.",
                e.getMessage()
            );
            System.err.println(errorMessage);
            Assert.fail(errorMessage, e);
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}