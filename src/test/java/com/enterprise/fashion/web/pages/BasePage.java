package com.enterprise.fashion.web.pages;

import com.enterprise.fashion.web.utils.MLogger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.Instant;

public class BasePage {

    private static final int LONG_WAIT = 25 ;
    private static final int MAX_WAIT = 2 * LONG_WAIT;
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver pDriver) {
        PageFactory.initElements(pDriver, this);
        wait = new WebDriverWait(pDriver, 10);
        driver = pDriver;
    }

    public WebDriverWait getWait() {
        return wait;
    }

    public WebDriverWait newWait(){
        wait = new WebDriverWait(driver, 10);
        return wait;
    }

    protected void waitUntilPageLoadedLongTimeout(WebElement referenceElement){
        MLogger.print(2,"Awaiting for the screen to be loaded. referenceElement. Max wait: {} seconds");

        Instant startTime = Instant.now();

        boolean elementVisible = isElementVisible( referenceElement, LONG_WAIT, 2000);

        Instant endTime = Instant.now();
        long elapsed = Duration.between( startTime, endTime ).toSeconds();
        MLogger.print(2,"Elapsed time awaiting for the screen to be loaded in seconds : " + elapsed);
        if( !elementVisible ){
            throw new TimeoutException("Long wait exceeded for the element "+referenceElement);
        }
    }

    /**
     * Method for inserting text in TextBox
     * @param element
     * @param textPattern
     */
    public void insertText(WebElement element, String textPattern){
        isElementVisible(element);
        element.click();
        element.sendKeys(textPattern);
        element.submit();
    }

    public WebElement hooverOverElement(WebElement element) {
        Actions action = new Actions(driver);
        action.moveToElement(element)
                .moveToElement(getDriver().findElement(By.cssSelector(".ajax_add_to_cart_button")))
                .build()
                .perform();

        WebElement addToCart = getDriver().findElement(By.cssSelector(".ajax_add_to_cart_button"));
        return addToCart;
    }

    /**
     * Method for clicking a webElement
     * @param element
     */
    public void clickInButton(WebElement element){
        isElementVisible(element);
        try{
            element.click();
            waitUntilElementDisappears(element,5,500);
        }catch (Exception ex){
            MLogger.print(3, "Element was not clickable at the moment");
        }
    }

    public void waitForElement(WebElement element){
        WebDriverWait wait = new WebDriverWait(driver, MAX_WAIT);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForElementToBePresent(WebElement element){
        WebDriverWait wait = new WebDriverWait(driver, MAX_WAIT);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Generic method to be used by the pages that have to wait for a specific condition
     * @param expectedCondition
     * @param timeoutInSeconds
     * @return
     */
    public WebDriverWait waitUntil(ExpectedCondition expectedCondition, int timeoutInSeconds){
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.ignoring( NoSuchElementException.class );
        wait.ignoring( org.openqa.selenium.WebDriverException.class);

        wait.until(expectedCondition);
        return wait;
    }

    /**
     *
     * @param expectedCondition to wait
     * @param timeoutInSeconds max timeout to wait (in seconds)
     * @param pollingMillis polling interval (in milliseconds)
     * @return
     */
    public WebDriverWait waitUntil( ExpectedCondition expectedCondition, int timeoutInSeconds, int pollingMillis){
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds, pollingMillis);
        wait.ignoring( NoSuchElementException.class );
        wait.ignoring( org.openqa.selenium.WebDriverException.class);

        wait.until(expectedCondition);
        return wait;
    }

    /**
     * Method for waiting until a given element disappears from the DOM
     * @param element
     * @param timeout
     * @param pollingInSeconds
     */
    public void waitUntilElementDisappears(WebElement element, int timeout, int pollingInSeconds) {
        long t1 = System.currentTimeMillis();
        //Waits until the element is not longer visible
        waitUntil(
                (wd) -> !isElementVisible( element , 1),
                timeout,
                pollingInSeconds*1000
        );

        long t2 = System.currentTimeMillis();
        MLogger.print(2,"Wait finished. Element not visible after " + (t2 -t1) + "seconds");
    }

    /**
     * @Deprecated: prefer to use isElementVisible
     * @param element
     * @return
     */
    @Deprecated
    public boolean isElementPresent(WebElement element){
        return isElementVisible( element, MAX_WAIT );
    }

    public boolean presenceOfElement(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, MAX_WAIT);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (TimeoutException ignored) {
            return false;
        } catch (Exception error) {
            MLogger.print(2,"Exception validating the presence of the element located by " + locator);
            return false;
        }
    }

    public boolean presenceOfElement(By locator,int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (TimeoutException ignored) {
            return false;
        } catch (Exception error) {
            MLogger.print(2, "Exception validating the presence of the element located by " + locator);
            return false;
        }
    }


    public boolean isElementVisible(WebElement element){
        return isElementVisible( element, 5 );
    }

    /**
     * Validates if the given element is visible in the page DOM
     * Note: If you want to validate that an element IS NOT present you should pass a low value to avoid waiting more than
     * necessary
     * @param element
     * @param timeOutInSeconds
     * @return
     */
    public boolean isElementVisible(WebElement element, int timeOutInSeconds){
        return isElementVisible( element, timeOutInSeconds, 500);
    }

    public boolean isElementVisible(WebElement element, int timeOutInSeconds, int pollingIntervalInMillis){
        WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds, pollingIntervalInMillis);
        boolean isVisible;
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            isVisible = true;
        } catch (TimeoutException ignored) {
            isVisible = false;
        } catch ( StaleElementReferenceException ignored) {
            MLogger.print(3,"Page.isElementVisible: StaleElementException caught");
            isVisible = false;
        } catch (WebDriverException wdException) {
            if( wdException.getMessage().contains("AccessibilityNodeInfo")) {
                MLogger.print(3,"isElementVisible: AccessibilityNodeInfo exception caught, checking visibility again");
                isVisible = isElementVisible(element, timeOutInSeconds, pollingIntervalInMillis);
            }else{
                MLogger.print(2,"Page.isElementVisible: WebDriverException message " +  wdException.getMessage() );
                isVisible = false;
            }
        } catch (Exception error) {
            MLogger.print(2,"Exception validating whether the element was visible" + element);
            isVisible = false;
        }
        MLogger.print(1,"is Element Visible? " + isVisible + " - class : "+element.getAttribute("class"));
        return isVisible;
    }

    public void explicitWaitInSeconds(double seconds){
        try {
            MLogger.print(1,"waiting seconds" +seconds);
            Thread.sleep((int)(seconds * 1000));
        } catch (InterruptedException ignored) {}
    }

    public void scrollUntilElement(WebElement element){
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].scrollIntoView();", element);
    }
/*
    public boolean swipeUntilFindElement(AppiumDriver driver, String direction, MobileElement wantedElement,int maxSwipes){
        boolean elementVisible = isElementVisible(driver, wantedElement, 1 );

        int swipeCount = 0;

        while( !elementVisible && swipeCount < maxSwipes){
            //swipeScreen should not be used because when the active screen can be closed by swiping the scroll closes it
            switch ( direction ) {
                case "up":
                    scroll(driver,0.5, 0.3, 0.5, 0.7);
                    break;
                case "down":
                    scroll(driver,0.5, 0.7, 0.5, 0.2);
                    break;
                case "right":
                    scroll(driver,0.8, 0.5, 0.125, 0.5);
                    break;
                case "left":
                    scroll(driver,0.2, 0.5, 0.875, 0.5);
                    break;
            }

            elementVisible = isElementVisible(driver, wantedElement, 0 );
            swipeCount++;
        }
        if(!elementVisible){
            logger.error("Element not found after having swiped {} times", swipeCount);
            return false;
        }
        return true;
    }

    private void scroll(AppiumDriver driver, double fromX, double fromY, double toX, double toY) {
        Dimension size = getScreenSize(driver);//driver.manage().window().getSize();
        int fromXInt = (int) (size.width * fromX);
        int fromYInt = (int) (size.height * fromY);
        int toXInt = (int) (size.width * toX);
        int toYInt = (int) (size.height * toY);

        new TouchAction(driver).press(PointOption.point(fromXInt, fromYInt))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                .moveTo(PointOption.point(toXInt, toYInt))
                .release().perform();
    }
*/


    public WebDriver getDriver() {
        return driver;
    }

    public void dispose() {
        if(driver!=null) {
            driver.quit();
        }
    }


}
