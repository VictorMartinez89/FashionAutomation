package com.enterprise.fashion.web.pages;

import com.enterprise.fashion.web.utils.MLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ResultsPage extends BasePage {

    @FindBy(className = "heading-counter")
    private WebElement lblCounterResults;

    @FindBy(css = "ul.product_list.grid.row")
    private WebElement productsContainer;

    @FindBy(css = "li[class*=\"ajax_block_product\"]")
    private List<WebElement> productList;

    @FindBy(className = "available-now")
    private WebElement btnInStock;

    @FindBy(css = ".right-block")
    private WebElement  lblProductContent;

    //Proceed Checkout PopUp
    @FindBy(css = "a[title=\"Proceed to checkout\"]")
    private WebElement btnProceedCheckout;

    @FindBy(css = ".ajax_block_products_total")
    private WebElement lblTotalProducts;

    @FindBy(css = ".layer_cart_row>.ajax_cart_shipping_cost")
    private WebElement lblTotalShipping;

    @FindBy(id= "layer_cart_product_price")
    private WebElement lblNetTotal;

    @FindBy(id = "uniform-selectProductSort")
    private WebElement btnSortProducts;


    public ResultsPage(WebDriver pDriver) {
        super(pDriver);
        waitUntilPageLoadedLongTimeout(productsContainer);
        scrollUntilElement(lblCounterResults);
    }

    public boolean isCounterResultVisible(){
        return isElementVisible(lblCounterResults);
    }

    public boolean isResultsSortButtonVisible(){
        return isElementVisible(btnSortProducts);
    }

    private static final String ADD_TO_CART_SELECTOR = "a[class*=\"ajax_add_to_cart_button\"]";

    public List<WebElement> getArticlesList(){
        List<WebElement> productsList = productList;
        return productsList;
    }

    public WebElement selectFirstElement(){
        return getArticlesList().get(0);
    }

    public ResultsPage clickAddToCart(WebElement product){
        WebElement addToCart = hooverOverElement(product);
        isElementVisible(addToCart);
        //Click AddToCart
        //WebElement element = product.findElement(By.cssSelector(ADD_TO_CART_SELECTOR));
        addToCart.click();
        return  new ResultsPage(getDriver());
    }

    public ResultsPage addToCartFirstResult(){
        WebElement firstProduct = selectFirstElement();
        isElementVisible(firstProduct);
        return clickAddToCart(firstProduct);
    }

    public boolean wasCheckoutPopUpDisplayed(){
        searchAdequateContext(btnProceedCheckout);
        return isElementVisible(btnProceedCheckout);
    }

    public boolean isTotalProductsValueDisplayed(){
        return  isElementVisible(lblTotalProducts);
    }

    public String getTotalShippingValue(){
        isElementVisible(lblTotalShipping);
        String totalShipping = lblTotalShipping.getText();
        MLogger.print(3,"Total Shipping Value : " + totalShipping);
        return totalShipping;
    }

    public boolean isTotalNetVisible(){
        return isElementVisible(lblNetTotal);
    }

    /**
     * Method for finding the correct Window
     * @param searchedElement
     */
    public void searchAdequateContext(WebElement searchedElement){
        String MainWindow=driver.getWindowHandle();

        // To handle all new opened window.
        Set<String> s1=driver.getWindowHandles();
        Iterator<String> i1=s1.iterator();

        MLogger.print(3, "Number of contexts available : " +s1.size());

        while(i1.hasNext())
        {
            String ChildWindow=i1.next();

            if(!MainWindow.equalsIgnoreCase(ChildWindow))
            {
                boolean isElementPresent = isElementVisible(searchedElement);

                if(isElementPresent) break;
                // Switching to Child window
                driver.switchTo().window(ChildWindow);
            }
        }
    }
}
