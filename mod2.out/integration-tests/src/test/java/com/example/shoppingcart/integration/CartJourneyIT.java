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
import org.openqa.selenium.WebElement;
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
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(classes = ShoppingCartApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ContextConfiguration(initializers = TestcontainersInitializer.class)
public class CartJourneyIT {

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
        Product product = new Product("prod-cart", "Cart Test Product", "Books", "Info", 25.0, 10, null);
        productRepository.save(product);
    }

    @AfterAll
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void loginAsUser() {
        driver.get("http://localhost:" + port + "/login");
        driver.findElement(By.id("email")).sendKeys("user@example.com");
        driver.findElement(By.id("password")).sendKeys(userRawPassword);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("/userHome"));
    }

    private void addProductToCart() {
        driver.get("http://localhost:" + port + "/products");
        // Find the form for the specific product and submit it
        WebElement productForm = driver.findElement(By.xpath("//td[text()='Cart Test Product']/following-sibling::td/form"));
        productForm.submit();
        driver.get("http://localhost:" + port + "/user/cart");
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.titleIs("Cart"));
        assertTrue(driver.getPageSource().contains("Cart Test Product"));
    }

    @Test
    void testUpdateCartQuantity() {
        loginAsUser();
        addProductToCart();

        // Update quantity
        WebElement quantityInput = driver.findElement(By.xpath("//td[text()='Cart Test Product']/following-sibling::td/form/input[@name='quantity']"));
        quantityInput.clear();
        quantityInput.sendKeys("3");
        driver.findElement(By.xpath("//td[text()='Cart Test Product']/following-sibling::td/form/button[text()='Update']")).click();

        // Verify quantity is updated
        new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.urlContains("/user/cart"));
        WebElement updatedQuantityInput = driver.findElement(By.xpath("//td[text()='Cart Test Product']/following-sibling::td/form/input[@name='quantity']"));
        assertEquals("3", updatedQuantityInput.getAttribute("value"));
    }

    @Test
    void testRemoveFromCart() {
        loginAsUser();
        addProductToCart();

        // Remove from cart
        driver.findElement(By.xpath("//td[text()='Cart Test Product']/following-sibling::td/a[text()='Remove']")).click();

        // Verify product is removed
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("/user/cart"));
        assertFalse(driver.getPageSource().contains("Cart Test Product"));
    }
}