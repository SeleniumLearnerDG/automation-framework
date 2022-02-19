package com.tests;

import com.automation.TestSessionInitiator;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GoogleSearchTest extends TestSessionInitiator {

    @Test(priority = 1)
    public void launchApplication() throws Throwable {
        googleSearch.launchApplicationUrl();
        Assert.assertEquals(googleSearch.getPageTitle(), "Google");
    }

    @Test(priority = 2)
    public void verifyGoogleSearchIsWorking() throws Throwable {
        googleSearch.enterSearchText("Amazon");
        Assert.assertEquals(googleSearchResult.getPageTitle(), "Amazon - Google Search");
    }

    @Test(priority = 3)
    public void verifyAmazonIsWorking() {
        googleSearchResult.clickOnAmazonSite();


    }
}
