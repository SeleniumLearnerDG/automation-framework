package com.automation;

import com.automation.utils.ConfigPropertyReader;
import com.automation.utils.TakeScreenshot;
import com.keywords.GoogleSearchActions;
import com.keywords.GoogleSearchResultActions;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.automation.utils.ConfigPropertyReader.getProperty;
import static com.automation.utils.YamlReader.setYamlFilePath;
//import com.automation.pojo.TopicNamePojo;


//import com.keywords.BasicCourseActions;

public class TestSessionInitiator {
    public static HashMap<String, String> configSettings;
    public static TestSessionInitiator test;
    protected GoogleSearchActions googleSearch;
    protected GoogleSearchResultActions  googleSearchResult;
    /**
     * Initiating the page objects
     */


    public TakeScreenshot takescreenshot;
    protected WebDriver driver;

    String takeScreenshot;
    String screenshotPath;
    private WebDriverFactory wdfactory;
    private String testname;

    public static HashMap<String, String> _getSessionConfig() {
        configSettings = ConfigPropertyReader.readAllPropertyVlauesFromConfigFile();
        Properties prop = System.getProperties();
        for (Object ob : configSettings.keySet()) {
            if (prop.containsKey(ob)) {
                configSettings.replace(ob.toString(), prop.get(ob).toString());
            }
        }
        return configSettings;
    }

    public WebDriver getDriver() {
        return this.driver;
    }

    private void _initPage() {
        googleSearch = new GoogleSearchActions(driver);
        googleSearchResult= new GoogleSearchResultActions(driver);
    }

    @BeforeSuite(alwaysRun = true)
    public void TestSessionInitiators() {
        test = new TestSessionInitiator();
        wdfactory = new WebDriverFactory();
        setYamlFilePath();
    }

    @BeforeClass
    public void launchMyApplication() {
        testInitiator();
    }

    public void testInitiator() {
        _configureBrowser();
        _initPage();
    }

    private void _configureBrowser() {
        driver = wdfactory.getDriver(_getSessionConfig());
        if (!_getSessionConfig().get("browser").toLowerCase().trim().equalsIgnoreCase("mobile")) {
			driver.manage().deleteAllCookies();
            try {
                String osName = System.getProperty("os.name").toLowerCase();
                if (osName.equals("mac os x")) {
                    driver.manage().window().setSize(new Dimension(2880, 1800));
                } else {
                    driver.manage().window().maximize();
                }

            } catch (WebDriverException ex) {
                driver.manage()
                        .timeouts()
                        .implicitlyWait(Integer.parseInt(getProperty("timeout")),
                                TimeUnit.SECONDS);
            }

        }
        driver.manage()
                .timeouts()
                .implicitlyWait(Integer.parseInt(getProperty("timeout")),
                        TimeUnit.SECONDS);
    }
//	private Map<String, String> _getSessionConfig() {
//		String[] configKeys = { "tier", "browser", "seleniumserver",
//				"seleniumserverhost", "timeout", "driverpath", "appiumServer",
//				"mobileDevice" };
//		Map<String, String> config = new HashMap<String, String>();
//		for (String string : configKeys) {
//			config.put(string, getProperty("./Config.properties", string));
//		}
//		return config;
//	}

    private void _configureBrowser(String testname) {
        driver = wdfactory.getDriver(_getSessionConfig(), testname);
        if (!_getSessionConfig().get("browser").toLowerCase().trim().equalsIgnoreCase("mobile")) {
//			driver.manage().deleteAllCookies();
            try {
                String osName = System.getProperty("os.name").toLowerCase();
                if (osName.equals("mac os x")) {
                    driver.manage().window().setSize(new Dimension(2880, 1800));
                } else {
                    driver.manage().window().maximize();
                }

            } catch (WebDriverException ex) {
                driver.manage()
                        .timeouts()
                        .implicitlyWait(Integer.parseInt(getProperty("timeout")),
                                TimeUnit.SECONDS);
            }
        }
        driver.manage()
                .timeouts()
                .implicitlyWait(Integer.parseInt(getProperty("timeout")),
                        TimeUnit.SECONDS);
    }

    public void openUrl(String url) {
        driver.get(url);
    }


    public void closeBrowserSession() {
        Reporter.log("[INFO]: The Test: " + this.testname.toUpperCase() + " COMPLETED!"
                + "\n", true);
        try {
            driver.quit();
            Thread.sleep(3000);// [INFO]: this to wait before you close every
            // thing
        } catch (Exception b) {
            b.getMessage();
        }
    }

    public void closeTestSession() {
        closeBrowserSession();
    }

    public String getCurrentURL() {
        return driver.getCurrentUrl();
    }

    @BeforeMethod(alwaysRun = true)
    public void logTestMethod(Method method) {
        String className = method.getDeclaringClass().getName();
        className = className.substring(className.lastIndexOf('.') + 1);
        testname = className;
        System.out.println(
                "**********************************************************" + "\n" + "Running Test:" + className + "."
                        + method.getName() + "\n" + "**********************************************************");
    }

    @AfterMethod(alwaysRun = true)
    public void take_screenshot_on_failure(ITestResult result) {
        takeScreenshot = _getSessionConfig().get("take-screenshot");
        screenshotPath = _getSessionConfig().get("screenshot-path");
        takescreenshot = new TakeScreenshot(testname, driver);
        takescreenshot.takeScreenShotOnException(result);
    }

    @AfterClass(alwaysRun = true)
    public void stop_test_session() {
        closeBrowserSession();
    }
}
