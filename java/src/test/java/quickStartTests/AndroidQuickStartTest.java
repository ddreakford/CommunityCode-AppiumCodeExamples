package quickStartTests;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import utils.ApplicationUploadUtil;
import utils.EnvironmentConfig;


public class AndroidQuickStartTest {

    private AndroidDriver driver = null;
    private ApplicationUploadUtil uploadUtil;

    @BeforeMethod
    public void setUp() throws MalformedURLException {
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
                .amend("digitalai:testName", "Run Quickstart test on Android device");
        driver = new AndroidDriver(new URL(EnvironmentConfig.getCloudUrl()), options);
    }

    @Test
    public void runQuickStartAndroidNative() {
        driver.rotate(ScreenOrientation.PORTRAIT);
        driver.findElement(By.id("com.experitest.ExperiBank:id/usernameTextField")).sendKeys("company");
        driver.findElement(By.id("com.experitest.ExperiBank:id/passwordTextField")).sendKeys("company");
        driver.findElement(By.id("com.experitest.ExperiBank:id/loginButton")).click();
        driver.findElement(By.id("com.experitest.ExperiBank:id/makePaymentButton")).click();
        driver.findElement(By.id("com.experitest.ExperiBank:id/phoneTextField")).sendKeys("0501234567");
        driver.findElement(By.id("com.experitest.ExperiBank:id/nameTextField")).sendKeys("John Snow");
        driver.findElement(By.id("com.experitest.ExperiBank:id/amountTextField")).sendKeys("50");
        driver.findElement(By.id("com.experitest.ExperiBank:id/countryTextField")).sendKeys("'Switzerland'");
        driver.findElement(By.id("com.experitest.ExperiBank:id/sendPaymentButton")).click();
        driver.findElement(By.id("android:id/button1")).click();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
