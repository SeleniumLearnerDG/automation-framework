package com.automation;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Reporter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static org.openqa.selenium.remote.CapabilityType.SUPPORTS_JAVASCRIPT;

public class WebDriverFactory {
    private String browser = "";
    public WebDriverFactory() {
    }
    public WebDriverFactory(String browserName) {
        browser = browserName;
    }
    private static final DesiredCapabilities capabilities = new DesiredCapabilities();
    public WebDriver getDriver(Map<String, String> seleniumconfig) {
        browser = System.getProperty("browser", browser);
        if (browser == null || browser.isEmpty()) {
            browser = seleniumconfig.get("browser");
        }
        Reporter.log("[INFO]: The test Browser is " + browser.toUpperCase() + " !!!", true);

        if (seleniumconfig.get("seleniumserver").equalsIgnoreCase("local")) {
            if (browser.equalsIgnoreCase("firefox")) {
                return getFirefoxDriver(seleniumconfig.get("driverpath"));
            } else if (browser.equalsIgnoreCase("chrome")) {
                return getChromeDriver(seleniumconfig.get("driverpath"));
            } else if (browser.equalsIgnoreCase("Safari")) {
                return getSafariDriver();
            } else if ((browser.equalsIgnoreCase("ie")) || (browser.equalsIgnoreCase("internetexplorer"))
                    || (browser.equalsIgnoreCase("internet explorer"))) {
                return getInternetExplorerDriver(seleniumconfig.get("driverpath"));
            }
            else if (browser.equalsIgnoreCase("mobile")) {
                return setMobileDriver(seleniumconfig);
            }
        }
        if (seleniumconfig.get("seleniumserver").equalsIgnoreCase("remote")) {
            return setRemoteDriver(seleniumconfig);
        }
        return new FirefoxDriver();
    }

    private WebDriver setRemoteDriver(Map<String, String> selConfig) {
        DesiredCapabilities cap = null;
        if (browser.equalsIgnoreCase("firefox")) {
            cap = DesiredCapabilities.firefox();
        } else if (browser.equalsIgnoreCase("chrome")) {
            cap = DesiredCapabilities.chrome();
        } else if (browser.equalsIgnoreCase("Safari")) {
            cap = DesiredCapabilities.safari();
        } else if ((browser.equalsIgnoreCase("ie")) || (browser.equalsIgnoreCase("internetexplorer"))
                || (browser.equalsIgnoreCase("internet explorer"))) {
            cap = DesiredCapabilities.internetExplorer();
        }
        String seleniuhubaddress = selConfig.get("seleniumserverhost");
        URL selserverhost = null;
        try {
            selserverhost = new URL(seleniuhubaddress);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        cap.setJavascriptEnabled(true);
        return new RemoteWebDriver(selserverhost, cap);
    }

    private static WebDriver getChromeDriver(String driverpath) {
         ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("start-maximized");
//        chromeOptions.addArguments("--incognito");
//        chromeOptions.addArguments("--headless");
//        chromeOptions.addArguments("--no-sandbox");
        WebDriverManager.chromedriver().setup();
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        cap.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        return  new ChromeDriver(chromeOptions);
    }

    private static WebDriver getInternetExplorerDriver(String driverpath) {

        System.out.println(driverpath + "IEDriverServer.exe");
        if (driverpath.endsWith(".exe")) {
            System.setProperty("webdriver.ie.driver", driverpath);
        } else {
            System.setProperty("webdriver.ie.driver", driverpath + "IEDriverServer.exe");
        }

        System.setProperty("java.net.preferIPv4Stack", "true");

        capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        capabilities.setCapability("ignoreZoomSetting", true);
        return new InternetExplorerDriver();
    }

    private static WebDriver getSafariDriver() {
        return new SafariDriver();
    }

    private static WebDriver getFirefoxDriver(String driverPath) {
        if (System.getProperty("os.name").toUpperCase().contains("windows".toUpperCase())) {
            System.setProperty("webdriver.gecko.driver", driverPath + "geckodriver.exe");
        } else {
            System.setProperty("webdriver.gecko.driver", driverPath + "geckodriver");
        }
        DesiredCapabilities firefoxOptions = new DesiredCapabilities();
        firefoxOptions.setCapability("browser.cache.disk.enable", false);
        firefoxOptions.setCapability("marionette", true);
        return new FirefoxDriver(firefoxOptions);
    }

    private static WebDriver getFirefoxDriverProfile(String driverPath) {
        if (System.getProperty("os.name").toUpperCase().contains("windows".toUpperCase())) {
            System.setProperty("webdriver.gecko.driver", driverPath + "geckodriver.exe");
        } else {
            System.setProperty("webdriver.gecko.driver", driverPath + "geckodriver");
        }
        DesiredCapabilities firefoxOptions = new DesiredCapabilities();
        firefoxOptions.setCapability("browser.cache.disk.enable", false);
        // String FilePath=System.getProperty("user.home") +
        // "\\Desktop\\DevelopOthers";
        String FilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\downloads";

        firefoxOptions.setCapability("browser.download.dir", FilePath);
        firefoxOptions.setCapability("browser.download.folderList", 2);
        firefoxOptions.setCapability("browser.helperApps.neverAsk.saveToDisk",
                "application/zip,application/pdf,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/powerpoint"
                        + "spreadsheetml.sheet,application/vnd.openxmlformats-officedocument.presentationml.presentation");
        firefoxOptions.setCapability("pdfjs.disabled", true);
        firefoxOptions.setCapability("plugin.scan.Acrobat", "99.0");
        firefoxOptions.setCapability("plugin.scan.plid.all", false);
        firefoxOptions.setCapability("marionette", true);
        return new FirefoxDriver(firefoxOptions);
    }

    private WebDriver setMobileDriver(Map<String, String> selConfig) {
        DesiredCapabilities cap = new DesiredCapabilities();
        String[] appiumDeviceConfig = selConfig.get("mobileDevice").split(":");

        cap.setCapability("deviceName", appiumDeviceConfig[0]);
        cap.setCapability("device", appiumDeviceConfig[1]);
        cap.setCapability("platformName", appiumDeviceConfig[1]);
        cap.setCapability("app", appiumDeviceConfig[2]);
        cap.setCapability(CapabilityType.VERSION, "5.0.2");
        cap.setCapability(CapabilityType.PLATFORM, "Windows");
        String appiumServerHostUrl = selConfig.get("appiumServer");
        URL appiumServerHost = null;
        try {
            appiumServerHost = new URL(appiumServerHostUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        cap.setJavascriptEnabled(true);
        System.out.println(appiumServerHostUrl);
        return new RemoteWebDriver(appiumServerHost, cap);
    }

    public WebDriver getDriver(Map<String, String> seleniumconfig, String testname) {
        browser = System.getProperty("browser", browser);
        if (browser == null || browser.isEmpty()) {
            browser = seleniumconfig.get("browser");
        }
        Reporter.log("[INFO]: The test Browser is " + browser.toUpperCase() + " !!!", true);

        if (seleniumconfig.get("seleniumserver").equalsIgnoreCase("local")) {
            if (browser.equalsIgnoreCase("firefox")) {
                switch (testname) {
                    case "Download_Resources_Test":
                        return getFirefoxDriverForSave(seleniumconfig.get("driverpath"));
                    // case "Lx_EditorChoice_Collections_Vedios_Tools_Test": return
                    // getFirefoxDriverForSavingDocx();
                    default:
                        return getFirefoxDriver(seleniumconfig.get("driverpath"));
                }

            } else if (browser.equalsIgnoreCase("chrome")) {
                return getChromeDriver(seleniumconfig.get("driverpath"));
            } else if (browser.equalsIgnoreCase("Safari")) {
                return getSafariDriver();
            } else if ((browser.equalsIgnoreCase("ie")) || (browser.equalsIgnoreCase("internetexplorer"))
                    || (browser.equalsIgnoreCase("internet explorer"))) {
                return getInternetExplorerDriver(seleniumconfig.get("driverpath"));
            }
            // TODO: treat mobile browser and separate instance on lines of
            // remote driver
            else if (browser.equalsIgnoreCase("mobile")) {
                return setMobileDriver(seleniumconfig);
            }
        }
        if (seleniumconfig.get("seleniumserver").equalsIgnoreCase("remote")) {
            return setRemoteDriver(seleniumconfig);
        }
        return new FirefoxDriver();
    }

    private static WebDriver getFirefoxDriverForSave(String driverPath) {
        System.out.println("+++INSIDE DOWNLOAD FILES METHOD+++");
        System.out.println(System.getProperty("os.name"));
        if (System.getProperty("os.name").toUpperCase().contains("windows".toUpperCase())) {
            System.setProperty("webdriver.gecko.driver", driverPath + "geckodriver.exe");
        } else {
            System.setProperty("webdriver.gecko.driver", driverPath + "geckodriver");
        }
//		DesiredCapabilities firefoxOptions = new DesiredCapabilities();
//		firefoxOptions.setCapability("browser.cache.disk.enable", false);
//		String FilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\downloadedLinks";
//
//		firefoxOptions.setCapability("browser.download.dir", FilePath);
//		firefoxOptions.setCapability("browser.download.folderList", 2);
//		firefoxOptions.setCapability("browser.helperApps.neverAsk.saveToDisk",
//				"application/zip,application/pdf,application/docx,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/powerpoint"
//						+ "spreadsheetml.sheet,application/vnd.openxmlformats-officedocument.presentationml.presentation");
//		firefoxOptions.setCapability("pdfjs.disabled", true);
//		firefoxOptions.setCapability("plugin.scan.Acrobat", "99.0");
//		firefoxOptions.setCapability("plugin.scan.plid.all", false);
//		return new FirefoxDriver(firefoxOptions);


        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("browser.cache.disk.enable", false);
        String FilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\downloadedLinks";

        profile.setPreference("browser.download.dir", FilePath);
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
                "application/zip,image/gif,application/pdf,application/docx,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/powerpoint"
                        + "spreadsheetml.sheet,application/vnd.openxmlformats-officedocument.presentationml.presentation");
        profile.setPreference("pdfjs.disabled", true);
        profile.setPreference("plugin.scan.Acrobat", "99.0");
        profile.setPreference("plugin.scan.plid.all", false);
        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability(FirefoxDriver.PROFILE, profile);
        return new FirefoxDriver(dc);
    }

}
