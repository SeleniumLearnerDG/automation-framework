package com.automation.utils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumWait {

    WebDriver driver;
    WebDriverWait wait;

    public int timeout;

    public SeleniumWait(WebDriver driver, int timeout) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeout);
        this.timeout = timeout;
    }

    public WebElement getWhenVisible(By locator) {
        WebElement element;
        element = (WebElement) wait.until(ExpectedConditions
                .visibilityOfElementLocated(locator));
        return element;
    }

    public WebElement getWhenClickable(By locator) {
        WebElement element;
        element = (WebElement) wait.until(ExpectedConditions.elementToBeClickable(locator));
        return element;
    }

    public boolean waitForPageTitleToBeExact(String expectedPagetitle) {
        return wait.until(ExpectedConditions.titleIs(expectedPagetitle)) != null;
    }

    public boolean waitForPageTitleToContain(String expectedPagetitle) {
        return wait.until(ExpectedConditions.titleContains(expectedPagetitle)) != null;
    }

    public WebElement waitForElementToBeVisible(WebElement element) {
        return (WebElement) wait.until(ExpectedConditions.visibilityOf(element));
    }


    public void waitForFrameToBeAvailableAndSwitchToIt(By locator) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
    }

    public List<WebElement> waitForElementsToBeVisible(List<WebElement> elements) {
        return (List<WebElement>) wait.until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    public void waitForElementsToBeVisibleInList(List<WebElement> elements){
        try{
            wait.until(ExpectedConditions.visibilityOfAllElements(elements));
        }catch(Exception ex){
            System.out.println("List Elements Are not Present.");
        }
    }

    public void waitForElementsListToBeExact(List<WebElement> elements,int size) {
        int timeOut=Integer.parseInt(ConfigPropertyReader.getProperty("timeout"));
        waiting:  	for (int i=0;i<=timeOut;i++){
            if(elements.size()==size){
                break waiting;
            }else{
                hardWait(1);
                continue waiting;
            }
        }
    }
    public boolean waitForElementToBeInVisible(By locator) {


        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator)) != null;

    }


    public void changeimpliciteTimeout(int sec)
    {
        driver.manage().timeouts().implicitlyWait(sec, TimeUnit.SECONDS);
    }
    public WebElement waitForElementToBeClickable(WebElement element) {
        return (WebElement) wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void clickWhenReady(By locator) {
        WebElement element = (WebElement) wait.until(ExpectedConditions
                .elementToBeClickable(locator));
        element.click();
    }


    public void waitForMsgToastToDisappear() {
        int i = 0;
        resetImplicitTimeout(1);
        try {
            while (driver.findElement(By.className("toast-message")).isDisplayed() && i <= timeout) {
                hardWait(1);
                i++;
            }
        } catch (Exception e) {
        }
        resetImplicitTimeout(timeout);
    }

    public void waitForElementToDisappear(WebElement element) {
        int i = 0;
        resetImplicitTimeout(2);
        try {
            while (element.isDisplayed() && i <= timeout) {
                hardWait(1);
                i++;
            }
        } catch (Exception e) {
        }
        resetImplicitTimeout(timeout);
    }

    public void resetImplicitTimeout(int newTimeOut) {
        try {
            driver.manage().timeouts().implicitlyWait(newTimeOut, TimeUnit.SECONDS);
        } catch (Exception e) {
        }
    }

    // TODO Implement Wait for page load for page synchronizations
    public void waitForPageToLoadCompletely() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("//*")));
    }

    public void  waitForPageUrlToContain(String str){
        int timeout=15;
        for(int i=0;i<=timeout;i++){
            if(StringUtils.containsIgnoreCase(driver.getCurrentUrl(), str))break;
            hardWait(1);
        }
    }

    public WebElement checkPresenceOfElementInDom(By loc)
    {
        return
                wait.until(ExpectedConditions.presenceOfElementLocated(loc));
    }

    public void hardWait(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void waitForElementTextToBe(String text,WebElement el){
        int timeout=15;
        waitForElementToBeVisible(el);
        for(int i=0;i<=timeout;i++){
            if(el.getText().equals(text))break;
            hardWait(1);
        }
    }

    public void waitForElementToAppear(WebElement element) {
        int i = 0;
        System.out.println("waiting For Element To Appear");
        System.out.println("element.isDisplayed()==="+ element.isDisplayed());
        resetImplicitTimeout(2);
//	        hardWait(1);
        try {
            System.out.println("try");
            while (!(element.isDisplayed()) && i <= timeout) {
                System.out.println("while");
                hardWait(1);
                i++;
                System.out.println(i + " Seconds");
            }
        } catch (StaleElementReferenceException e) {
            System.out.println("exception thrown");
        }
        System.out.println("exitig loop");
        resetImplicitTimeout(timeout);
        System.out.println("exiting function");
    }

    public void waitForExactValueOfElement(WebElement element, String ExpectedText) {
        int i = 0;
        resetImplicitTimeout(1);
        try {
            while (!((element.getText()).equals(ExpectedText)) && i <= timeout) {
                hardWait(1);
                i++;
                System.out.println(i + " Seconds");
            }

        } catch (Exception e) {
            System.out.println();
        }
        resetImplicitTimeout(timeout);
    }

}
