package com.enterprise.fashion.web.data;

import org.testng.annotations.DataProvider;


public class searchDP {

    @DataProvider(name = "products")
    public Object[][] dataProviderFunction(){
        return new Object[][]{
                {"Shirt"}
        };
    }

}
