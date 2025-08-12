package com.example.shoppingcart.integration;

import com.example.shoppingcart.ShoppingCartApplication;
import com.example.shoppingcart.model.User;
import com.example.shoppingcart.service.UserService;
import com.example.shoppingcart.repository.UserRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = ShoppingCartApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ContextConfiguration(initializers = TestcontainersInitializer.class)
@Transactional
public class UserJourneyIT {

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private TestEntityManager entityManager;

    private WebDriver driver;

    @BeforeAll
    void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run Chrome in headless mode
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);

        // Create an admin user for testing
        User adminUser = new User("Admin User", "admin@example.com", passwordEncoder.encode("adminpass"), "1234567890", "Admin Address", "12345", "ADMIN");
        entityManager.persistAndFlush(adminUser);
    }

    @AfterAll
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void contextLoads() {
        assertTrue(true);
    }

    @Test
    void testUserRegistrationPersistence() {
        User user = new User("Persistence Test User", "persistence@example.com", "testpassword", "9876543210", "456 Persistence Ave", "54321");
        userService.registerUser(user);

        User foundUser = userRepository.findByEmail("persistence@example.com").orElse(null);

        assertNotNull(foundUser);
        assertEquals("Persistence Test User", foundUser.getName());
        assertEquals("persistence@example.com", foundUser.getEmail());
    }

    @Test
    void testUserServiceAuthentication() {
        User user = new User("Auth Test User", "auth@example.com", "authpassword", "1112223333", "789 Auth St", "67890");
        userService.registerUser(user);

        boolean isAuthenticated = userService.authenticateUser("auth@example.com", "authpassword");

        assertTrue(isAuthenticated);
    }

    @Test
    void testUserRegistrationAndLogin() {
        // Test Registration
        driver.get("http://localhost:" + port + "/register");
        assertEquals("Register", driver.getTitle());

        driver.findElement(By.id("name")).sendKeys("Integration Test User");
        driver.findElement(By.id("mobile")).sendKeys("1234567890");
        driver.findElement(By.id("email")).sendKeys("integration@example.com");
        driver.findElement(By.id("address")).sendKeys("123 Integration St");
        driver.findElement(By.id("pinCode")).sendKeys("12345");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Verify redirect to login page after registration
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));
        assertEquals("Login", driver.getTitle());

        // Test Login
        driver.findElement(By.id("email")).sendKeys("integration@example.com");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Verify redirect to user home page after successful login
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("/userHome"));
        assertTrue(driver.getCurrentUrl().contains("/userHome"));
        assertEquals("User Home", driver.getTitle());
    }

    @Test
    void testAdminAddProduct() {
        // Admin Login
        driver.get("http://localhost:" + port + "/login");
        driver.findElement(By.id("email")).sendKeys("admin@example.com");
        driver.findElement(By.id("password")).sendKeys("adminpass");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("/adminHome"));
        assertTrue(driver.getCurrentUrl().contains("/adminHome"));
        assertEquals("Admin Home", driver.getTitle());

        // Navigate to Add Product Page
        driver.get("http://localhost:" + port + "/admin/addProduct"); // Assuming this URL
        assertEquals("Add Product", driver.getTitle()); // Assuming this title

        // Fill Product Details
        driver.findElement(By.id("prodName")).sendKeys("Test Product");
        driver.findElement(By.id("prodType")).sendKeys("Electronics");
        driver.findElement(By.id("prodInfo")).sendKeys("A product for integration testing.");
        driver.findElement(By.id("prodPrice")).sendKeys("99.99");
        driver.findElement(By.id("prodQuantity")).sendKeys("10");
        // For image, we might need to handle file uploads, which is more complex.
        // For now, we'll skip image upload or assume a default/no image scenario.

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Verify redirect to View Products page after adding
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("/admin/viewProducts"));
        assertTrue(driver.getCurrentUrl().contains("/admin/viewProducts"));
        assertEquals("View Products", driver.getTitle()); // Assuming this title

        // Verify product is in the list
        assertTrue(driver.getPageSource().contains("Test Product"));
        assertTrue(driver.getPageSource().contains("99.99"));
    }
}