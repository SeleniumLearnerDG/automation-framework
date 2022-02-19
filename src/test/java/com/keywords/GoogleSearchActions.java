package com.keywords;

import com.automation.getpageobjects.GetPage;
import com.automation.utils.YamlReader;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class GoogleSearchActions extends GetPage {

   public GoogleSearchActions(WebDriver driver){
        super(driver, "GoogleSearchPage");
    }
    public void launchApplicationUrl() {
        driver.get(YamlReader.getData("app_url"));
    }


    public void enterSearchText(String searchText) {
        element("search_box").sendKeys(searchText, Keys.ENTER);

    }
}
