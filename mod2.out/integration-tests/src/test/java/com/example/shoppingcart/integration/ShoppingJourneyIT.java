package com.example.shoppingcart.integration;

import com.example.shoppingcart.ShoppingCartApplication;
import com.example.shoppingcart.model.User;
import com.example.shoppingcart.repository.UserRepository;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.repository.ProductRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.ContextConfiguration;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = ShoppingCartApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ContextConfiguration(initializers = TestcontainersInitializer.class)
public class ShoppingJourneyIT {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private JavaMailSender javaMailSender;

    private String userRawPassword = "userpass";

    private WebDriver driver;

    @BeforeAll
    void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);

        // Create a regular user for testing
        User regularUser = new User("Regular User", "user@example.com", passwordEncoder.encode(userRawPassword), "0987654321", "User Address", "54321", "USER");
        userRepository.save(regularUser);

        // Create a product for testing
        Product product = new Product("1", "Test Product", "Electronics", "A product for testing", 99.99, 10, null);
        productRepository.save(product);
    }

    @AfterAll
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testUserShoppingJourney() {
        // Test Login
        driver.get("http://localhost:" + port + "/login");
        assertEquals("Login", driver.getTitle());

        driver.findElement(By.id("email")).sendKeys("user@example.com");
        driver.findElement(By.id("password")).sendKeys(userRawPassword);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Verify redirect to user home page after successful login
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("/userHome"));
        assertTrue(driver.getCurrentUrl().contains("/userHome"));
        assertEquals("User Home", driver.getTitle());

        // Navigate to products page
        driver.get("http://localhost:" + port + "/products");
        assertEquals("Products", driver.getTitle());

        // Add product to cart
        driver.findElement(By.id("addToCartForm")).submit();

        // Go to cart and verify
        driver.get("http://localhost:" + port + "/user/cart");
        assertEquals("Cart", driver.getTitle());
        assertTrue(driver.getPageSource().contains("Test Product"));

        // Proceed to checkout
        driver.findElement(By.xpath("//a[contains(text(),'Checkout')]")).click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("/user/checkout"));
        assertEquals("Checkout", driver.getTitle());

        // Fill out shipping details and place order
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(By.id("address")));
        driver.findElement(By.id("address")).sendKeys("123 Test Street");
        driver.findElement(By.id("city")).sendKeys("Testville");
        driver.findElement(By.id("state")).sendKeys("Testland");
        driver.findElement(By.id("zip")).sendKeys("12345");
        driver.findElement(By.xpath("//button[contains(text(),'Place Order')]")).click();

        // Verify order confirmation
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.titleIs("My Orders"));
        assertEquals("My Orders", driver.getTitle());
        assertTrue(driver.getPageSource().contains("Test Product"));
    }
}
