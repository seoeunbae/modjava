
package com.example.shoppingcart.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CartPage {

    private final WebDriver driver;

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isProductInCart(String productName) {
        return driver.findElements(By.xpath("//td[contains(text(), '" + productName + "')]")).size() > 0;
    }

    public void proceedToCheckout() {
        driver.findElement(By.id("checkout-button")).click();
    }
}
