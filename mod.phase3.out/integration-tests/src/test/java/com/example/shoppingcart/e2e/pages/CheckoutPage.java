
package com.example.shoppingcart.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage {

    private final WebDriver driver;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

    public void fillShippingInformation(String name, String address, String city, String zip) {
        driver.findElement(By.id("name")).sendKeys(name);
        driver.findElement(By.id("address")).sendKeys(address);
        driver.findElement(By.id("city")).sendKeys(city);
        driver.findElement(By.id("zip")).sendKeys(zip);
    }

    public void submitOrder() {
        driver.findElement(By.id("submit-order-button")).click();
    }

    public boolean isOrderConfirmed() {
        return driver.findElements(By.id("order-confirmation")).size() > 0;
    }
}
