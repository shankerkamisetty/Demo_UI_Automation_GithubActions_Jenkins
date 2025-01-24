package page_tests;

import base.AppConstants;
import base.BasePage;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

import static utils.ExtentReportHelper.getReportObject;

public class BaseTest {
    private static final ExtentReports reports = getReportObject();
    private static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected static ThreadLocal<ExtentTest> testLogger = new ThreadLocal<>();
    protected WebDriver driver;
    protected String browser;

    @Parameters({"browserName"})
    @BeforeMethod
    public void setupTest(@Optional String browserName, ITestResult iTestResult) {
        ChromeOptions co = new ChromeOptions();
        FirefoxOptions fo = new FirefoxOptions();

        if (browserName != null) {
            browser = browserName;
        } else {
            browser = AppConstants.browserName;
        }

        logger.info("Browser name is:" + browser);

        if (browser.equalsIgnoreCase("chrome")) {
            if (AppConstants.platform.equalsIgnoreCase("local")) {
                co.addArguments("--headless");
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(co);

                // for Selenium Grid
            } else if (AppConstants.platform.equalsIgnoreCase("remote")) {
                co.setPlatformName("linux");
//                co.addArguments("--headless");
                co.setPageLoadStrategy(PageLoadStrategy.EAGER);
                try {
                    //remote webdriver url for Selenium grid standalone browser
//                    driver = new RemoteWebDriver(new URL("http://localhost:4441"), co);

                    //remote webdriver url for Selenium Grid
//                    driver = new RemoteWebDriver(new URL("http://localhost:4444"), co);

                    // grid firefox & mention your system IP address instead of localhost
//                    driver = new RemoteWebDriver(new URL("http://192.168.1.3:4444/wd/hub"), co);
                    driver = new RemoteWebDriver(new URL("http://selenium-hub:4444/wd/hub"), co);

                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }

                //for GitHub actions
            } else if (AppConstants.platform.equalsIgnoreCase("remote_git")) {
                co.addArguments("--headless"); //for GitHub actions
                co.addArguments("--disable-gpu");
                co.addArguments("--no-sandbox");
                co.addArguments("--remote-allow-origins=*");
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(co);
            } else {
                logger.error("Platform not supported!!");
            }

        } else if (browser.equalsIgnoreCase("firefox")) {
            if (AppConstants.platform.equalsIgnoreCase("local")) {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                //driver = new FirefoxDriver(fo);
            } else if (AppConstants.platform.equalsIgnoreCase("remote")) {
                fo.setPlatformName("linux");
                fo.setPageLoadStrategy(PageLoadStrategy.EAGER);
                try {
                    //remote webdriver url for Selenium standalone browser
//                    driver = new RemoteWebDriver(new URL("http://localhost:4442"), fo);

                    //remote webdriver url for Selenium Grid
//                    driver = new RemoteWebDriver(new URL("http://localhost:4444"), fo);

                    // grid firefox & mention your system IP address instead of localhost
//                    driver = new RemoteWebDriver(new URL("http://192.168.1.3:4444/wd/hub"), fo);
                    driver = new RemoteWebDriver(new URL("http://selenium-hub:4444/wd/hub"), fo);

                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }

                //for GitHub actions
            } else if (AppConstants.platform.equalsIgnoreCase("remote_git")) {
                fo.addArguments("--headless"); //for GitHub actions
                fo.addArguments("--disable-gpu");
                fo.addArguments("--no-sandbox");
                //  fo.addArguments("--remote-allow-origins=*"); not required for GitHub actions execution flow
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver(fo);
            } else {
                logger.error("Platform not supported!!!");
            }
        } else {
            logger.info("Invalid Browser Name");
        }

        ExtentTest test = reports.createTest(iTestResult.getMethod().getMethodName());
        testLogger.set(test);
        testLogger.get().log(Status.INFO, "Driver Start Time: " + LocalDateTime.now());
    }

    @AfterMethod
    public void tearDownTest(ITestResult iTestResult) {
        if (iTestResult.isSuccess()) {
            testLogger.get().log(Status.PASS, MarkupHelper.createLabel(iTestResult.getMethod().getMethodName() + " is successful!!", ExtentColor.GREEN));
        } else {
            testLogger.get().log(Status.FAIL, "Test failed due to: " + iTestResult.getThrowable());
            String screenshot = BasePage.getScreenshot(iTestResult.getMethod().getMethodName() + ".jpg", driver);
            testLogger.get().fail(MediaEntityBuilder.createScreenCaptureFromBase64String(BasePage.convertImg_Base64(screenshot)).build());
        }

        testLogger.get().log(Status.INFO, "Driver End Time: " + LocalDateTime.now());

        driver.quit();
    }

    @AfterClass
    public void flushTestReport() {
        reports.flush();
    }
}
