package com.enterprise.fashion.web.tests;

import com.enterprise.fashion.web.config.MyDriver;
import com.enterprise.fashion.web.utils.MLogger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import java.io.IOException;
import java.util.logging.FileHandler;

public class BaseTest {
    MyDriver myDriver;
    String URL_Homepage = "";

    public WebDriver getDriver(){
        return myDriver.getDriver();
    }

    @BeforeSuite
    @Parameters({"Browser"})
    public void instanceDriver(String Browser) throws IOException {
        MLogger.initializeFile();
        MLogger.print(0, "Browser instance of " + Browser);
        this.myDriver =new MyDriver(Browser);
    }

    @BeforeTest
    @Parameters({"URL_Homepage"})
    public void setURL(String homepage){
        MLogger.print(3, "Setting URL Before Test - ESPN Homepage");
        this.URL_Homepage = homepage;
        this.getDriver().manage().window().maximize();
    }

    @AfterMethod
    public void closeHomePage(){
        MLogger.print(3, "Clearing the instances for a new test case");

    }

    @AfterTest
    public void closeDriver(){
        MLogger.print(0, "Ending up the Browser instance ");
        this.getDriver().close();
    }

    @AfterSuite(alwaysRun=true)
    public void afterSuite() {
        MLogger.print(0, "Closing Suite Instance");
    }

    }
