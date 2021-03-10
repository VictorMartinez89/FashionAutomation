package com.enterprise.fashion.web.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MyDriver {

    private WebDriver driver;
    public MyDriver(String browser) {
        switch(browser) {
            case "remoteFirefox":
                try {
                    System.setProperty("webdriver.gecko.driver", "C:\\Users\\va.martinez\\Documents\\Programs\\Drivers\\Selenium_WebDriver\\Browsers\\Firefox\\geckodriver.exe" );
                    driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.firefox());
                    driver.wait();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            case "firefox":

                /*System.setProperty("webdriver.gecko.driver", "C:\\Users\\va.martinez\\Documents\\Programs\\Drivers\\Selenium_WebDriver\\Browsers\\Firefox\\geckodriver.exe" );
                FirefoxProfile profile = new FirefoxProfile();
                profile.setPreference("permissions.default.desktop-notification", 1);
                DesiredCapabilities capabilities=DesiredCapabilities.firefox();
                capabilities.setCapability(FirefoxDriver.PROFILE, profile);*/
                //System.setProperty("webdriver.gecko.driver", "/Users/va.martinez/Documents/Globant/Studying/Testing/Globant/Academy/Project2/ESPN/WEB/WebAutomation/src/test/resources/driver/geckodriver" );
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setCapability("marionette", true);
                driver = new FirefoxDriver(firefoxOptions);
                break;


            case "chrome":
                //System.setProperty("webdriver.chrome.driver", "/Users/va.martinez/Documents/Globant/Studying/Testing/Globant/Academy/Project2/ESPN/WEB/WebAutomation/src/test/resources/driver/chromedriver" );
                //System.setProperty("webdriver.chrome.driver", "src/test/resources/driver" );
                //ChromeOptions options = new ChromeOptions();
                //Map<String, Object> prefs = new HashMap<String, Object>();
                //prefs.put("download.default_directory", "src/test/resources/driver");
                //prefs.put("profile.default_content_setting_values.notifications", 2);
                //options.setExperimentalOption("prefs", prefs);
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();

                break;

            default :
                break;
        }
    }
        public WebDriver getDriver() {
            if(this.driver ==null) {
                new MyDriver("chrome");
            }
            return this.driver;
        }

        public void quit(){
            if(null!=driver){
                driver = null;
            }
        }
}

