
package com.example.shoppingcart.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {

    private final WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isUserLoggedIn() {
        return driver.findElements(By.id("logout-button")).size() > 0;
    }

    public void addProductToCart() {
        // This assumes a product is available on the home page.
        // A more robust test would search for a product first.
        driver.findElement(By.cssSelector(".add-to-cart-button")).click();
    }
}
