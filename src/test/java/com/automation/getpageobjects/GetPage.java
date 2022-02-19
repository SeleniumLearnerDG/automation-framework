package com.automation.getpageobjects;

import com.automation.TestSessionInitiator;
import com.automation.utils.ConfigPropertyReader;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import static com.automation.getpageobjects.ObjectFileReader.getELementFromFile;
import static com.automation.utils.ConfigPropertyReader.getProperty;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;


//import com.automation.utils.LayoutValidation;

import static com.automation.utils.YamlReader.getData;

public class GetPage extends  BaseUi    {
    protected WebDriver webdriver;
    String pageName;
//    LayoutValidation layouttest;
    public static String environment = "";
    public static String testLanguage = "english";
    public static String bluegreen ;
    public static String applicationToTest = "Lx";
    public static String baseUrl = "";

    public GetPage(WebDriver driver, String pageName) {
        super(driver, pageName);
        this.webdriver = driver;
        this.pageName = pageName;
//        layouttest = new LayoutValidation(driver, pageName);
    }

    protected void getEnvironment() {
        String env = ConfigPropertyReader.getProperty("tier");
        try {
            if (env.equalsIgnoreCase("QA")) {
                environment = "http://qa-myhbp.org/hmm12";
            } else if (env.equalsIgnoreCase("PROD")) {
                environment = "https://myhbp.org/hmm12";
            } else if (env.equalsIgnoreCase("ssoqa")) {
                environment = "https://testsso2.qa-myhbp.org/";
            } else if (env.equalsIgnoreCase("ssoprod")) {
                environment = "https://test.myhbp.org";
            } else {
                System.out.println("Default environment will be QA.");
                environment = "http://qa-myhbp.org/hmm12";

            }
            System.out.println("[INFO]: Set current environment URL--->" + environment);
        } catch (Exception e) {
            System.out.println("The Exception got in passing the environment URL is-> " + e);

        }

    }

    public void launchAxUrl() {
        String completeURL = "";
        driver.manage().deleteAllCookies();

        if (environment.contains("sso")) {
            completeURL = new StringBuilder(environment).append("hmm12admin").toString();
        } else {
            completeURL = new StringBuilder(environment).append("admin").toString();
        }
        System.out.println("[INFO]: The complete AX URL is " + completeURL);

        driver.get(completeURL);
        hardWait(1);
        logMessage("Page Title:::"+getPageTitle());
        logMessage("Page Title:"+driver.getTitle());
        Assert.assertTrue(driver.getTitle().contains("Harvard Business Publishing"),"[ASSERT FAILED]: Current title is: "+driver.getTitle());
        logMessage("[INFO]: Launched Admin URL");
    }

    // public void launchAdminURL()
    // {
    // driver.get(YamlReader.getData("adminbase_url"));
    //
    // }

//    public void testPageLayout(List<String> tagsToBeTested) {
//        layouttest.checklayout(tagsToBeTested);
//    }
//
//    public void testPageLayout(List<String> browserSizes, List<String> tagsToBeTested) {
//        layouttest.checklayout(browserSizes, tagsToBeTested);
//    }
//
//    public void testPageLayout(String[] browserSizes, String[] tagToBeTested) {
//        testPageLayout(Arrays.asList(browserSizes), Arrays.asList(tagToBeTested));
//    }
//
//    public void testPageLayout(String... tagToBeTested) {
//        testPageLayout(Arrays.asList(tagToBeTested));
//    }
//
//    public void testPageLayout() {
//        testPageLayout(Arrays.asList(getProperty("./Config.properties", "browser")));
//    }

    // TODO: put this in right place, create dedicated class for frame and
    // window handlers
    protected void switchToNestedFrames(String frameNames) {
        switchToDefaultContent();
        String[] frameIdentifiers = frameNames.split(":");
        for (String frameId : frameIdentifiers) {
            wait.waitForFrameToBeAvailableAndSwitchToIt(getLocator(frameId.trim()));
        }
    }

    protected WebElement element(String elementToken) {
        return element(elementToken, "");
    }

    protected WebElement getelement(String elementToken) {
        return getelement(elementToken, "");
    }

    protected WebElement element(String elementToken, String replacement) throws NoSuchElementException {
        WebElement elem = null;
        try {

            elem = wait.waitForElementToBeVisible(webdriver.findElement(getLocator(elementToken, replacement)));
        } catch (NoSuchElementException excp) {
            fail(logMessage(
                    "[ASSERT FAILED]: Element " + elementToken + " not found on the " + this.pageName + " !!!"));
        } catch (NullPointerException npe) {

        }
        return elem;
    }

    protected WebElement getelement(String elementToken, String replacement) throws NoSuchElementException {
        WebElement elem = null;
        try {
            elem = wait.waitForElementToBeVisible(webdriver.findElement(getLocator(elementToken, replacement)));
        } catch (NoSuchElementException excp) {
            logMessage(" ");
        } catch (NullPointerException npe) {

        }
        return elem;
    }

    protected WebElement childOfElement(WebElement el, String elementToken, String replacement)
            throws NoSuchElementException {
        WebElement elem = null;
        try {
            elem = wait.waitForElementToBeVisible(el.findElement(getLocator(elementToken, replacement)));
        } catch (NoSuchElementException excp) {
            fail(logMessage(
                    "[ASSERT FAILED]: Element " + elementToken + " not found on the " + this.pageName + " !!!"));
        } catch (NullPointerException npe) {

        }
        return elem;
    }

    protected void checkAndWaitForLoadingSpinnerToDisappear(String elementToken) {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        WebElement elem = null;

        try {
            elem = wait.waitForElementToBeVisible(webdriver.findElement(getLocator(elementToken, "")));

            for (int i = 1;; i++) {
                try {
                    if (elem.isDisplayed())
                        hardWait(1);
                } catch (Exception e) {
                    logMessage("[INFO]: Content loaded! ");
                    break;
                }
            }

        } catch (NoSuchElementException excp) {

            logMessage("[INFO]: Loading spinner not found");
        } catch (NullPointerException npe) {

            logMessage("[INFO]: Loading spinner not found");
        }

        driver.manage().timeouts().implicitlyWait(Integer.parseInt(getProperty("timeout")), TimeUnit.SECONDS);

    }

    protected WebElement childOfElement(WebElement el, String elementToken) throws NoSuchElementException {
        return childOfElement(el, elementToken, "");
    }

    protected List<WebElement> elements(String elementToken, String replacement) {
        try {
            return wait.waitForElementsToBeVisible(webdriver.findElements(getLocator(elementToken, replacement)));
        } catch (Exception e) {
            return wait.waitForElementsToBeVisible(webdriver.findElements(getLocator(elementToken, replacement)));
        }

    }

    protected List<WebElement> elemsconstructed_dynamically(String elementToken, String replacement)
            throws NoSuchElementException {
        List<WebElement> elemList = null;
        try {
            elemList = wait.waitForElementsToBeVisible(
                    webdriver.findElements(getLocatorByReplacing(elementToken, replacement)));
        } catch (NoSuchElementException excp) {
            fail(logMessage(
                    "[ASSERT FAILED]: Element " + elementToken + " not found on the " + this.pageName + " !!!"));
        } catch (NullPointerException npe) {

        } catch (StaleElementReferenceException exe) {
            System.out.println("Inside catch ....");
            hardWait(1);
            elemList = wait.waitForElementsToBeVisible(
                    webdriver.findElements(getLocatorByReplacing(elementToken, replacement)));
        }
        return elemList;
    }

    protected List<WebElement> elementsFromElement(WebElement el, String elementToken, String replacement) {
        wait.waitForElementsToBeVisible(el.findElements(getLocator(elementToken, replacement)));
        return el.findElements(getLocator(elementToken, replacement));
    }

    protected List<WebElement> elements(String elementToken) {
        return elements(elementToken, "");
    }

    protected List<WebElement> elementsFromElement(WebElement el, String elementToken) {
        return elementsFromElement(el, elementToken, "");
    }

    protected boolean isElementDisplayed(String elementName, String elementTextReplace) {
        wait.waitForElementToBeVisible(element(elementName, elementTextReplace));
        boolean result = element(elementName, elementTextReplace).isDisplayed();
        assertTrue(result, logMessage("[ASSERT FAILED]: element '" + elementName + "with text " + elementTextReplace
                + "' is not displayed."));
        logMessage("[ASSERT PASSED]: element " + elementName + " with text " + elementTextReplace + " is displayed.");
        return result;
    }

    protected void verifyElementText(String elementName, String expectedText) {
        wait.waitForElementToBeVisible(element(elementName));
        assertEquals(element(elementName).getText().trim(), expectedText,
                logMessage("[ASSERT FAILED]: Text of the page element '" + elementName + "' is not as expected: "));
        logMessage("[ASSERT PASSED]: element " + elementName + " is visible and Text is " + expectedText);
    }

    protected void verifyElementTextContains(String elementName, String expectedText) {
        wait.waitForElementToBeVisible(element(elementName));
        assertThat(logMessage("[ASSERT FAILED]: Text of the page element '" + elementName + "' is not as expected: "),
                element(elementName).getText().trim(), containsString(expectedText));
        logMessage("[ASSERT PASSED]: element " + elementName + " is visible and Text is " + expectedText);
    }

    protected boolean isElementNotDisplayed(String elementName) {
        boolean result = false;
        try {
            if (webdriver.findElement(getLocator(elementName)).isDisplayed())
                result = false;
            else
                result = true;
        } catch (Exception ex) {
            result = true;
        }
        assertTrue(result, "TEST FAILED: element '" + elementName + "' is displayed.");
        logMessage("TEST PASSED: element " + elementName + " is not displayed.");
        return result;
    }

    protected boolean isElementDisplayed(String elementName) {
        wait.waitForElementToBeVisible(element(elementName));
        boolean result = element(elementName).isDisplayed();
        assertTrue(result, "[ASSERT FAILED]: element '" + elementName + "' is not displayed.");
        logMessage("[ASSERT PASSED]: element " + elementName + " is displayed.");
        return result;
    }

    public boolean Is_Element_Displayed(String token) {
        boolean status = false;
        try {
            Assert.assertTrue(element(token).isDisplayed());
            status = true;
        } catch (AssertionError er) {
            status = false;
        }
        return status;
    }

    protected boolean verifySizeOfTheList(String elementName, int k) {
        wait.waitForElementsToBeVisible(elements(elementName));
        boolean result = elements(elementName).size() == k;
        assertTrue(result,
                "[ASSERT FAILED]: elements '" + elementName + "' is not displayed, actual number of elements are: "
                        + elements(elementName).size() + "Should have been " + k);
        logMessage("[ASSERT PASSED]: elements " + elementName + " are displayed.");
        return result;
    }

    protected boolean isElementEnabled(String elementName, boolean expected) {
        wait.waitForElementToBeVisible(element(elementName));
        boolean result = expected && element(elementName).isEnabled();
        assertTrue(result, "[ASSERT FAILED]: element '" + elementName + "' is  ENABLED :- " + !expected);
        logMessage("[ASSERT PASSED]: element " + elementName + " is enabled :- " + expected);
        return result;
    }

    protected By getLocator(String elementToken) {
        return getLocator(elementToken, "");
    }

    protected By getLocator(String elementToken, String replacement) {
        String[] locator = getELementFromFile(this.pageName, elementToken);
        locator[2] = locator[2].replaceAll("\\$\\{.+\\}", replacement);
        return getBy(locator[1].trim(), locator[2].trim());
    }

    protected WebElement elemconstructed_dynamically(String elementToken, String replacement)
            throws NoSuchElementException {
        WebElement elem = null;
        try {
            elem = wait
                    .waitForElementToBeVisible(webdriver.findElement(getLocatorByReplacing(elementToken, replacement)));
        } catch (NoSuchElementException excp) {
            fail(logMessage(
                    "[ASSERT FAILED]: Element " + elementToken + " not found on the " + this.pageName + " !!!"));
        } catch (NullPointerException npe) {

        } catch (StaleElementReferenceException exe) {
            System.out.println("Inside catch ....");
            hardWait(1);
            elem = wait
                    .waitForElementToBeVisible(webdriver.findElement(getLocatorByReplacing(elementToken, replacement)));
        }
        return elem;
    }

    protected By getLocatorByReplacing(String elementToken, String replacement) {
        String[] locator = getELementFromFile(this.pageName, elementToken);
        locator[2] = StringUtils.replace(locator[2], "$", replacement);
        // locator[2] = StringUtils.replace(locator[2], "%", replacement2);
        return getBy(locator[1].trim(), locator[2].trim());
    }

    protected By getLocator(String elementToken, String replacement1, String replacement2) {
        String[] locator = getELementFromFile(this.pageName, elementToken);
        locator[2] = StringUtils.replace(locator[2], "$", replacement1);
        locator[2] = StringUtils.replace(locator[2], "%", replacement2);
        return getBy(locator[1].trim(), locator[2].trim());
    }

    private By getBy(String locatorType, String locatorValue) {
        switch (Locators.valueOf(locatorType)) {
            case id:
                return By.id(locatorValue);
            case xpath:
                return By.xpath(locatorValue);
            case css:
                return By.cssSelector(locatorValue);
            case name:
                return By.name(locatorValue);
            case classname:
                return By.className(locatorValue);
            case linktext:
                return By.linkText(locatorValue);
            case tagName:
                return By.tagName(locatorValue);
            default:
                return By.id(locatorValue);
        }
    }

    public static void copyUsingKeyboard(WebElement e) {
        e.click();
        e.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.chord(Keys.CONTROL, "c"));
    }

    public static String getTextFromClipboard() throws UnsupportedFlavorException, IOException {

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        String result = (String) clipboard.getData(DataFlavor.stringFlavor);
        return result;
    }

    // public void launchLxHomePage(String title)
    // {
    // driver.get(YamlReader.getData("base_url"));
    // Assert.assertTrue(getPageTitle().contains("Harvard Business Publishing -
    // Login"),"[INFO]: LX home page not launched successfully");
    // logMessage("[ASSERT PASSED]: lx home page launched successfully");
    // }

    protected List<WebElement> getListOfElementsUsingStaticCss(String locator) {
        return driver.findElements(By.cssSelector(locator));
    }

    public void hover_Element_By_Javascript_Event(String hoverElementToken) {
        hardWait(2);

        String js = "var targetElement = document.evaluate(\"" + hoverElementToken
                + "\",document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null ).singleNodeValue;" +

                "var evt=new Event('mouseover');" +

                "targetElement.dispatchEvent(evt);";

        executeJavascript(js);

    }

    /**
     * @param locator
     * @return TO be used for avoiding check for visibility/implicit wait
     *         condition
     */
    protected WebElement getWebElementUsingStaticCss(String locator) {
        return driver.findElement(By.cssSelector(locator));
    }

    protected WebElement getWebElelementUsingStaticXpath(String locator) {
        return driver.findElement(By.xpath(locator));
    }

    protected List<WebElement> getListOfWebElementUsingStaticXpath(String locator) {
        return driver.findElements(By.xpath(locator));
    }

    public String getWindowHandle() {
        return driver.getWindowHandle();
    }

    protected void selectThePurchaseProduct_Ax(String product) {
        wait.hardWait(2);
        try {
            getWebElementUsingStaticCss(".navbar-btn.dropdown-toggle.pull-right").click();
        } catch (WebDriverException ex) {
            clickByJavascript(getWebElementUsingStaticCss(".navbar-btn.dropdown-toggle.pull-right"));
        }

        wait.hardWait(2);
        getWebElementUsingStaticCss(".btn.btn-default.dropdown-toggle.text-right.ng-binding").click();
        wait.hardWait(2);
        List<WebElement> li = getListOfWebElementUsingStaticXpath(".//*[@id='sidebar']/div/div/ul/li/a");
        for (WebElement e : li) {
            if (e.getText().equalsIgnoreCase(product)) {
                // e.click();
                clickByJavascript(e);
                wait.hardWait(2);
                break;
            }
        }
    }

    public long getRandomNumericValue() {
        long value = System.currentTimeMillis();
        return value;
    }

    public void languageToTest() {
        testLanguage = ConfigPropertyReader.getProperty("language");
    }

    public String isExecutionOnBlueGreen() {
        return bluegreen= TestSessionInitiator.configSettings.get("bluegreen");
    }

    public String executionOnApplication() {
        return applicationToTest=TestSessionInitiator.configSettings.get("applicationToTest");
    }

    public void getBaseUrlToTest() {
        isExecutionOnBlueGreen();
        executionOnApplication();
        baseUrl = getData("base_url");
        if(bluegreen.toLowerCase().trim().equals("yes")
                && applicationToTest.toLowerCase().trim().equals("lx")){
            baseUrl = getData("bg_lx_base_url");
        }else if(bluegreen.toLowerCase().trim().equals("yes")
                && applicationToTest.toLowerCase().trim().equals("ax")){
            baseUrl = getData("bg_ax_base_url");
        }
    }

    public void getApplicationToTestOnBlueGreen(String application) {
        isExecutionOnBlueGreen();
        baseUrl = getData("base_url");
        if(bluegreen.toLowerCase().trim().equals("yes")
                && application.toLowerCase().trim().equals("lx")){
            baseUrl = getData("bg_lx_base_url");
        }else if(bluegreen.toLowerCase().trim().equals("yes")
                && application.toLowerCase().trim().equals("ax")){
            baseUrl = getData("bg_ax_base_url");
        }
    }

}
