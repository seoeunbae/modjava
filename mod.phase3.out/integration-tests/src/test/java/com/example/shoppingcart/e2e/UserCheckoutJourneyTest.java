
package com.example.shoppingcart.e2e;

import com.example.shoppingcart.e2e.pages.CartPage;
import com.example.shoppingcart.e2e.pages.CheckoutPage;
import com.example.shoppingcart.e2e.pages.HomePage;
import com.example.shoppingcart.e2e.pages.LoginPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class UserCheckoutJourneyTest {

    private static final String APP_URL = "http://localhost:8080";
    private static WebDriver driver;

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test")
            .withInitScript("init_db.sql");

    @BeforeAll
    public static void setUp() throws Exception {
        // Initialize WebDriver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Initialize database
        try (Connection conn = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())) {
            try (Statement stmt = conn.createStatement()) {
                // Schema creation and data seeding should be handled by the init script.
                // Here you could add more dynamic seeding if needed.
            }
        }
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testUserCheckoutJourney() {
        driver.get(APP_URL);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("testuser@example.com", "password123");

        HomePage homePage = new HomePage(driver);
        assertTrue(homePage.isUserLoggedIn());
        homePage.addProductToCart();

        CartPage cartPage = new CartPage(driver);
        driver.get(APP_URL + "/cart");
        assertTrue(cartPage.isProductInCart("Sample Product"));
        cartPage.proceedToCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.fillShippingInformation("Test User", "123 Test St", "Test City", "12345");
        checkoutPage.submitOrder();

        assertTrue(checkoutPage.isOrderConfirmed());
    }
}
