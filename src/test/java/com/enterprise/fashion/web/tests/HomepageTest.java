package com.enterprise.fashion.web.tests;

import com.enterprise.fashion.web.data.searchDP;
import com.enterprise.fashion.web.pages.ResultsPage;
import com.enterprise.fashion.web.utils.MLogger;
import com.enterprise.fashion.web.config.MyDriver;
import com.enterprise.fashion.web.pages.Homepage;
import org.testng.Assert;
import org.testng.annotations.*;

public class HomepageTest extends BaseTest {


    private Homepage home;
    private ResultsPage resultsPage;

    @BeforeClass
    public void loadHomePage(){
        MLogger.print(3, "Creating POM of Fashion Homepage");
        home =  new Homepage(getDriver(), URL_Homepage);
    }

    @Test(priority = 1)
    public void AssertFashionHomePage(){
        MLogger.print(3, "Assert TEST Home page");

        boolean isLogoVisible = home.isLogoVisible();
        Assert.assertTrue( isLogoVisible, "Logo was visible as expected");

        boolean sliderValidation = home.isSliderComplete();
        Assert.assertTrue(sliderValidation, "Slider was displayed as expected");
    }

    @Test(priority = 2, dataProvider = "products", dataProviderClass= searchDP.class )
    public void AssertSearchResultsPage(String productSearch){
        MLogger.print(3, "Assert TEST Results page");
        resultsPage = home.searchArticle(productSearch);

        boolean resultCounterValidation = resultsPage.isCounterResultVisible();
        Assert.assertTrue(resultCounterValidation, "Result Counter was displayed as expected");

        boolean sortProductsVisibility = resultsPage.isResultsSortButtonVisible();
        Assert.assertTrue(sortProductsVisibility, "Sort Product element was displayed as expected");

    }

    @Test(priority = 3)
    public void AssertCheckoutProductsPage(){
        MLogger.print(3, "Assert TEST Add to Cart page");
        resultsPage.addToCartFirstResult();

        boolean isCheckoutDisplayed = resultsPage.wasCheckoutPopUpDisplayed();
        Assert.assertTrue(isCheckoutDisplayed, "Checkout PopUp was displayed as expected");

        boolean isTotalProductsVisible = resultsPage.getTotalShippingValue().contains("2.00");
        Assert.assertTrue(isTotalProductsVisible, "Total Shipping Value was establisged in 2.00");

        boolean isNetTotalVisible = resultsPage.isTotalNetVisible();
        Assert.assertTrue(isNetTotalVisible, "Total net was visible ");
    }
}
