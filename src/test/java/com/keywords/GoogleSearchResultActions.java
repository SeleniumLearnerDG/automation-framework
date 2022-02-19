package com.keywords;

import com.automation.getpageobjects.GetPage;
import org.openqa.selenium.WebDriver;

public class GoogleSearchResultActions extends GetPage {

    public GoogleSearchResultActions(WebDriver driver){
        super(driver, "GoogleSearchResultPage");
    }


    public void clickOnAmazonSite() {
        click(element("heading3"));
    }
}
