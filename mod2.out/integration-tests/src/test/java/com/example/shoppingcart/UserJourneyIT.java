package com.example.shoppingcart;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserJourneyIT {

    @LocalServerPort
    private int port;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Container
    public BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer<>()
            .withCapabilities(new org.openqa.selenium.chrome.ChromeOptions());

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    public void testUserJourney() {
        WebDriver driver = new RemoteWebDriver(chrome.getSeleniumAddress(), new org.openqa.selenium.chrome.ChromeOptions());

        driver.get("http://host.testcontainers.internal:" + port);

        // Registration
        driver.findElement(By.linkText("Register")).click();
        driver.findElement(By.id("name")).sendKeys("Test User");
        driver.findElement(By.id("email")).sendKeys("test@example.com");
        driver.findElement(By.id("mobile")).sendKeys("1234567890");
        driver.findElement(By.id("address")).sendKeys("123 Test Street");
        driver.findElement(By.id("pincode")).sendKeys("12345");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.id("register-btn")).click();

        // Login
        driver.findElement(By.id("email")).sendKeys("test@example.com");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.id("login-btn")).click();

        // Add to cart
        driver.findElement(By.linkText("Add to Cart")).click();

        // View cart
        driver.findElement(By.linkText("Cart")).click();
        WebElement productInCart = driver.findElement(By.xpath("//td[contains(text(), 'Test Product')]"));
        assertEquals("Test Product", productInCart.getText());

        // Place order
        driver.findElement(By.linkText("Place Order")).click();

        // Verify order
        driver.findElement(By.linkText("My Orders")).click();
        WebElement orderProduct = driver.findElement(By.xpath("//td[contains(text(), 'Test Product')]"));
        assertEquals("Test Product", orderProduct.getText());

        driver.quit();
    }
}
