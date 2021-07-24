import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public abstract class BaseTest {

    public static final String HUB_URL = "http://localhost:4444/wd/hub";

    private static boolean remoteWebDriver = false;

    static {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(HUB_URL + "/status").openConnection();
            try {
                con.setRequestMethod("GET");
System.out.println(con.getResponseCode());
                remoteWebDriver = con.getResponseCode() == HttpURLConnection.HTTP_OK;
            } finally {
                con.disconnect();
            }
        } catch (IOException ignore) {}

        if (!remoteWebDriver) {
            WebDriverManager.chromedriver().setup();
        }
    }

    public static boolean isRemoteWebDriver() {
        return remoteWebDriver;
    }

    private WebDriver driver;
    private WebDriverWait wait;

    protected WebDriver getDriver() {
        if (driver == null) {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--window-size=1920,1080");

            if (isRemoteWebDriver()) {

                chromeOptions.setHeadless(true);
                chromeOptions.addArguments("--disable-gpu");

                try {
                    driver = new RemoteWebDriver(new URL(HUB_URL), chromeOptions);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                driver = new ChromeDriver(chromeOptions);
            }

            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }

        driver.get("http://jenkins123:8080/");
        driver.findElement(By.id("j_username")).sendKeys("admin");
        driver.findElement(By.xpath("//input[@name='j_password']")).sendKeys("159875321");
        driver.findElement(By.className("submit-button")).click();

        return driver;
    }

    protected void stopDriver() {
        driver.quit();
        wait = null;
    }

    protected WebDriverWait getWait() {
        if (wait == null) {
            wait = new WebDriverWait(driver, 10);
        }
        return wait;
    }
}
