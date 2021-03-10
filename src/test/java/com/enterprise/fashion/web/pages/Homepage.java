package com.enterprise.fashion.web.pages;

import com.enterprise.fashion.web.config.MyDriver;
import com.enterprise.fashion.web.utils.MLogger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class Homepage extends BasePage {

    @FindBy(css = "a.homefeatured")
    private WebElement btnPopular;

    @FindBy(id = "search_query_top")
    private WebElement tbSearchBox;

    @FindBy(id = "header_logo")
    private WebElement lblLogo;

    @FindBy(className = "homeslider-description")
    private List<WebElement> sliderContent;

    public Homepage(WebDriver pDriver, String url) {
        super(pDriver);
        this.driver.get(url);
        MLogger.print(3, "Instance of Home Page already created");
        waitUntilPageLoadedLongTimeout(btnPopular);
    }

    private static final String SLIDE_CONTENT = "EXCEPTEUR";

    /**
     * Method for searching an article making use of the Top Bar search element
     * @param articlePattern
     */
    public ResultsPage searchArticle(String articlePattern){
       insertText(tbSearchBox,articlePattern);

       return new ResultsPage(getDriver());
    }

    public boolean isLogoVisible(){
        return isElementVisible(lblLogo);
    }

    public boolean isSliderComplete(){
        isElementVisible(tbSearchBox);
        List<WebElement> slides = sliderContent;

        return slides
                .stream()
                .filter(
                        slide-> slide.getText().contains(SLIDE_CONTENT)
                ).findAny().isPresent();
    }
}
