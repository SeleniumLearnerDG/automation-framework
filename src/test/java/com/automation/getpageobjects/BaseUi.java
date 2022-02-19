package com.automation.getpageobjects;

import com.automation.utils.SeleniumWait;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import com.automation.utils.ConfigPropertyReader;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.automation.getpageobjects.ObjectFileReader.getPageTitleFromFile;
import static com.automation.utils.ConfigPropertyReader.getProperty;

public class BaseUi {
    protected WebDriver driver;
    protected SeleniumWait wait;
    private String pageName;

    protected BaseUi(WebDriver driver, String pageName) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        this.pageName = pageName;
        this.wait = new SeleniumWait(driver, Integer.parseInt(getProperty("Config.properties", "timeout")));
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    protected String logMessage(String message) {
        Reporter.log(message, true);
        return message;
    }

    protected String getCurrentURL() {
        return driver.getCurrentUrl();
    }

    protected void verifyPageTitleExact() {
        String pageTitle = getPageTitleFromFile(pageName);
        verifyPageTitleExact(pageTitle);
    }

    protected void verifyPageTitleExact(String expectedPagetitle) {
        if (((expectedPagetitle == "") || (expectedPagetitle == null) || (expectedPagetitle.isEmpty()))
                && (getProperty("browser").equalsIgnoreCase("chrome"))) {
            expectedPagetitle = getCurrentURL();
            System.out.println(expectedPagetitle);
        }
        try {
            wait.waitForPageTitleToBeExact(expectedPagetitle.toString());
            logMessage("[ASSERT PASSED]: PageTitle for " + pageName + " is exactly: '" + expectedPagetitle + "'");
        } catch (TimeoutException ex) {
            logMessage("[ASSERT FAILED]: PageTitle for " + pageName + " is not exactly: '" + expectedPagetitle
                    + "'!!!\n instead it is :- " + driver.getTitle());
        }
    }

    /**
     * Verification of the page title with the title text provided in the page
     * object repository
     */
    protected void verifyPageTitleContains() {
        String expectedPagetitle = getPageTitleFromFile(pageName).trim();
        verifyPageTitleContains(expectedPagetitle);
    }

    /**
     * this method will get page title of current window and match it partially
     * with the param provided
     *
     * @param expectedPagetitle
     *            partial page title text
     */
    protected void verifyPageTitleContains(String expectedPagetitle) {
        if (((expectedPagetitle == "") || (expectedPagetitle == null) || (expectedPagetitle.isEmpty()))
                && (getProperty("browser").equalsIgnoreCase("chrome"))) {
            expectedPagetitle = getCurrentURL();
        }
        try {
            wait.waitForPageTitleToContain(expectedPagetitle.toString());
            logMessage("[ASSERT PASSED]: PageTitle for " + pageName + " contains: '" + expectedPagetitle + "'.");
        } catch (TimeoutException exp) {
            logMessage("[ASSERT FAILED]: As actual Page Title for '" + pageName
                    + "' does not contain expected Page Title : '" + expectedPagetitle + "'.");
        }

    }


    protected void verifyPageUrlContains(String expectedPageUrl) {
        try {
            wait.waitForPageToLoadCompletely();
        } catch (Exception ex) {

        }
        wait.waitForPageUrlToContain(expectedPageUrl);
        String currenturl = getCurrentURL();
        Assert.assertTrue(currenturl.toLowerCase().trim().contains(expectedPageUrl.toLowerCase()),
                logMessage("[INFO]: verifying: URL - " + currenturl + " of the page '" + pageName + "' contains: "
                        + expectedPageUrl));
        logMessage("[ASSERT PASSED]: URL of the page " + pageName + " contains:- " + expectedPageUrl);

    }

    protected WebElement getElementByIndex(List<WebElement> elementlist, int index) {
        return elementlist.get(index);
    }

    protected WebElement getElementByExactText(List<WebElement> elementlist, String elementtext) {
        WebElement element = null;
        for (WebElement elem : elementlist) {
            if (elem.getText().equalsIgnoreCase(elementtext.trim())) {
                element = elem;
            }
        }
        // FIXME: handle if no element with the text is found in list No element
        // exception
        if (element == null) {
        }
        return element;
    }

    protected WebElement getElementByContainsText(List<WebElement> elementlist, String elementtext) {
        WebElement element = null;
        for (WebElement elem : elementlist) {
            if (elem.getText().contains(elementtext.trim())) {
                element = elem;
            }
        }
        // FIXME: handle if no element with the text is found in list
        if (element == null) {
        }
        return element;
    }

    protected void switchToFrame(WebElement element) {
        // switchToDefaultContent();
        wait.waitForElementToBeVisible(element);
        driver.switchTo().frame(element);
    }

    public void switchToFrame(int i) {
        driver.switchTo().frame(i);
    }

    public void switchToFrame(String id) {
        driver.switchTo().frame(id);
    }

    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    public void executeJavascript(String script) {
        ((JavascriptExecutor) driver).executeScript(script);
    }

    protected Object executeJavascriptWithReturn(String script) {
        return ((JavascriptExecutor) driver).executeScript(script);
    }

    protected Map<String, String> executeJavascriptWithReturn(String script, WebElement e) {
        Object o = ((JavascriptExecutor) driver).executeScript(script, e);
        Map<String, String> map = (Map<String, String>) o;
        return map;
    }

    protected String executeJavascriptWithReturnString(String script) {
        Object o = ((JavascriptExecutor) driver).executeScript(script);
        String str = (String) o;
        return str;
    }

    protected void executeJavascript(String script, WebElement e) {
        ((JavascriptExecutor) driver).executeScript(script, e);
    }

    protected String executeJavascript_With_Return(String script, WebElement e) {
        return String.valueOf(((JavascriptExecutor) driver).executeScript(script, e));
    }

    protected void hover(WebElement element) {
        Actions hoverOver = new Actions(driver);
        hoverOver.moveToElement(element).build().perform();
    }

    protected void handleAlert() {
        try {
            switchToAlert().accept();
            logMessage("Alert handled..");
            driver.switchTo().defaultContent();
        } catch (Exception e) {
            System.out.println("No Alert window appeared...");
        }
    }


    protected boolean isVerticalScrollBarPresentOnPage(){
        String execScript = "return document.documentElement.scrollHeight>document.documentElement.clientHeight;";
        JavascriptExecutor scrollBarPresent = (JavascriptExecutor) driver;
        Boolean result = (Boolean) (scrollBarPresent.executeScript(execScript));
        return result;
    }


//	protected void changeWindow(int i) {
//		Set<String> windows = driver.getWindowHandles();
//		if (i > 0) {
//			for (int j = 0; j < 9; j++) {
//				System.out.println("Windows: " + windows.size());
//
//				if (windows.size() >= 2) {
//					try {
//						Thread.sleep(5000);
//					} catch (Exception ex) {
//						ex.printStackTrace();
//					}
//					break;
//				}
//				windows = driver.getWindowHandles();
//			}
//		}
//		String wins[] = windows.toArray(new String[windows.size()]);
//		driver.switchTo().window(wins[i]);
//
//		System.out.println("Title: " + driver.switchTo().window(wins[i]).getTitle());
//	}


    Set<String> windows;
    String wins[];

    protected void changeWindow(int i)
    {
        if (i > 0)
        {
            for (int j = 0; j < 15; j++)
            {
                windows = driver.getWindowHandles();
                logMessage("Windows: " + windows.size());

                if (windows.size() >= 2)
                {
                    try
                    {
                        Thread.sleep(5000);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    break;
                }
            }
        }
        wins = windows.toArray(new String[windows.size()]);
        driver.switchTo().window(wins[i]);
        // TODO This condition needs updated when we have a strategy for determining desktop vs mobile
        //if (!(ConfigPropertyReader.getProperty("browser").contains("mobile")))
        wait.hardWait(2);
        driver.manage().window().maximize();

        logMessage("Title: " + driver.switchTo().window(wins[i]).getTitle());
    }

    protected void closeCurrentAndSwitchToPreviousWindow(String parentHandle) {
        driver.close();
        hardWait(3);
        driver.switchTo().window(parentHandle);
    }

    protected Alert switchToAlert() {
        WebDriverWait wait = new WebDriverWait(driver, 1);
        return wait.until(ExpectedConditions.alertIsPresent());
    }

    protected void selectProvidedTextFromDropDown(WebElement el, String text) {
        wait.waitForElementToBeVisible(el);
        scrollDown(el);
        Select sel = new Select(el);
        sel.selectByVisibleText(text);
    }

    protected void scrollDown(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected void hoverClick(WebElement element) {
        Actions hoverClick = new Actions(driver);
        hoverClick.moveToElement(element).click().build().perform();
    }

    protected void clickWithoutScroll(WebElement element) {
        try {
            wait.waitForElementToBeVisible(element);
            element.click();
        } catch (StaleElementReferenceException ex1) {
            wait.waitForElementToBeVisible(element);
            element.click();
            logMessage("Clicked Element " + element + " after catching Stale Element Exception");
        } catch (Exception ex2) {
            logMessage("Element " + element + " could not be clicked! " + ex2.getMessage());
        }
    }

    /*
     * protected void click(WebElement element) { // try { //
     * wait.waitForElementToBeVisible(element); // scrollDown(element); //
     * element.click(); // } catch (StaleElementReferenceException ex1) { //
     * wait.waitForElementToBeVisible(element); // scrollDown(element); //
     * element.click(); // logMessage("Clicked Element " + element +
     * " after catching Stale Element Exception"); // } catch (Exception ex2) {
     * // logMessage("Element " + element + " could not be clicked! " +
     * ex2.getMessage()); // }
     *
     * wait.waitForElementToBeClickable(element);
     * wait.waitForElementToBeVisible(element); try { scrollDown(element);
     * element.click(); logMessage("Clicked element "+ element); } catch
     * (StaleElementReferenceException ex1) { scrollDown(element);
     * element.click(); logMessage("Clicked Element " + element +
     * " after catching Stale Element Exception");
     * }catch(ElementNotVisibleException e){ clickByJavascript(element); } catch
     * (WebDriverException ex3) { clickByJavascript(element); }catch(Exception
     * e){
     *
     * wait.waitForElementToBeClickable(element); scrollDown(element);
     * performClickByActionBuilder(element); logMessage("Clicked Element " +
     * element + " after catching WebDriver Exception"); } }
     */

    protected void click(WebElement element) {
        wait.waitForElementToBeVisible(element);
        wait.waitForElementToBeClickable(element);
        try {
            element.click();
            logMessage("Clicked Element " + element);
        } catch (StaleElementReferenceException ex1) {
            scrollDown(element);
            clickByJavascript(element);
        } catch (WebDriverException ex3) {
            scrollDown(element);
            performClickByActionBuilder(element);
            logMessage("Clicked Element " + element + " after catching WebDriver Exception");
        }
    }

    public void launchSpecificUrl(String url) {
        driver.get(url);
        logMessage("[INFO]: The url launched is: " + url);
    }

    public void clickByJavascript(WebElement element) {
        wait.waitForElementToBeVisible(element);
        scrollDown(element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }


    // public void click(WebElement element) {
    // wait.waitForElementToBeClickable(element);
    // wait.waitForElementToBeVisible(element);
    // try {
    // scrollDown(element);
    // element.click();
    // logMessage("Clicked element "+ element);
    // } catch (StaleElementReferenceException ex1) {
    // scrollDown(element);
    // element.click();
    // logMessage("Clicked Element " + element
    // + " after catching Stale Element Exception");
    // }catch(ElementNotVisibleException e){
    // clickByJavascript(element);
    // }
    // catch (WebDriverException ex3) {
    // clickByJavascript(element);
    // }catch(Exception e){
    //
    // wait.waitForElementToBeClickable(element);
    // scrollDown(element);
    // performClickByActionBuilder(element);
    // logMessage("Clicked Element " + element
    // + " after catching WebDriver Exception");
    // }
    // }

    public static String getCurrentBrowser() {
        String browser = System.getProperty("browser");
        if (browser == null || browser.isEmpty()) {
            browser = ConfigPropertyReader.getProperty("browser");
        }
        return browser;
    }

    public void performClickByActionBuilder(WebElement element) {
        Actions builder = new Actions(driver);
        builder.moveToElement(element).build().perform();
        builder.moveToElement(element).click().perform();
    }

    public void hardWait(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean checkIfCurrentBrowserIsIE() {
        if (getCurrentBrowser().equals("ie"))
            return true;
        else
            return false;
    }

    public void hardWaitForIE(int seconds) {
        if (getCurrentBrowser().equals("ie")) {
            try {
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void waitUntilElementIsEnabled(WebElement el) {
        int i = 1;
        wait: while (!el.isEnabled()) {
            if (i > 15) {
                break wait;
            }
            try {
                i++;
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void waitUntilWebElementListIsLoaded(List<WebElement> li) {
        int timer = 1;
        wait: while (!(li.size() > 0)) {
            if (timer > 10) {
                break wait;
            }
            try {
                timer++;
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /* Additional debug methods: */

    // Added the following FIX for Se ver 2.50.1 window maximizing problem

    protected void maximizeWindow() {
        try {
            driver.manage().window().maximize();
        } catch (WebDriverException ex) {
            System.out.println("[INFO]: Appears to be a mobile browser, Skipping maximizing");
        }
    }

    public void printPageURl() {
        System.out.println("PAge URL: " + driver.getCurrentUrl());
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }

    public String getPageUrl() {
        return driver.getCurrentUrl();
    }

    public List<WebElement> get_listOf(List<WebElement> el, String tagName, String classContent) {
        List<WebElement> li = new ArrayList<WebElement>();
        int i = 0;
        do {
            List<WebElement> list = el.get(i).findElements(By.tagName(tagName));
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getAttribute("class").contains(classContent)) {
                    WebElement e = list.get(j);
                    li.add(e);
                }
            }
            i++;
            System.out.println("The value of i after increment ==>" + i);
        } while (i < el.size());
        return li;
    }

    public void closeCurrentBrowser() {
        driver.quit();
    }

    public void navigateBrowserBack() {
        driver.navigate().back();
    }

    public void switchDriverToNewWindow(String currentUrlContainsText) {
        wait.hardWait(7);
        ArrayList<String> win = new ArrayList<String>(driver.getWindowHandles());

        driver.switchTo().window(win.get(0));
        if (getCurrentURL().contains(currentUrlContainsText)) {
            driver.switchTo().window("");
            driver.switchTo().window(win.get(1));
        }
        driver.manage().window().maximize();

    }

    protected String openInNewTab() {
        String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL, Keys.RETURN);
        return selectLinkOpeninNewTab;
    }

    protected void switchToNextTab(int count) {
        String browser = getProperty("Config.properties", "browser");
        logMessage("Browser:" + browser);
//		if (browser.equalsIgnoreCase("firefox")) {
//			for (int i = 0; i < count; i++)
//				driver.findElement(By.xpath("//body")).sendKeys(Keys.CONTROL + "\t");
//		} else {
        // for (String handle1 : driver.getWindowHandles()) {
        // System.out.println(handle1);
        // driver.switchTo().window(handle1); }
        ArrayList tabs = new ArrayList(driver.getWindowHandles());
        System.out.println("tabs size:" + tabs.size());
        driver.switchTo().window((String) tabs.get(count));

        logMessage("Page Title:" + getPageTitle());
    }

    public void switchToPreviousTab(int count) {
        String browser = getProperty("Config.properties", "browser");
        logMessage("Browser:" + browser);
        ArrayList tabs = new ArrayList(driver.getWindowHandles());
        System.out.println("tabs size:" + tabs.size());
        driver.switchTo().window((String) tabs.get(count));

    }

    public void hardWaitForIEBrowser(int seconds) {
        if (getProperty(
                "Config.properties", "browser").equalsIgnoreCase("IE")
                || getProperty(
                "Config.properties", "browser")
                .equalsIgnoreCase("ie")
                || getProperty(
                "Config.properties", "browser")
                .equalsIgnoreCase("internetexplorer")) {
            wait.hardWait(seconds);
        }
    }

    public void clickUsingXpathInJavaScriptExecutor(WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    public void sendKeysUsingXpathInJavaScriptExecutor(WebElement element,
                                                       String text) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].setAttribute('value', '" + text
                + "')", element);
    }
}
