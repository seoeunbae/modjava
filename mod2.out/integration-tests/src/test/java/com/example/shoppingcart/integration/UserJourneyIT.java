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
import jakarta.persistence.EntityManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.io.File;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = ShoppingCartApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ContextConfiguration(initializers = TestcontainersInitializer.class)
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
    private EntityManager entityManager;

    @Autowired
    private AuthenticationManager authenticationManager;

    private String adminRawPassword = "adminpass"; // Store the raw password
    private String userRawPassword = "userpass"; // Store the raw password

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
        User adminUser = new User("Admin User", "admin@example.com", passwordEncoder.encode(adminRawPassword), "1234567890", "Admin Address", "12345", "ADMIN");
        userRepository.save(adminUser);

        // Create a regular user for testing
        User regularUser = new User("Regular User", "user@example.com", passwordEncoder.encode(userRawPassword), "0987654321", "User Address", "54321", "USER");
        userRepository.save(regularUser);
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
    void testUserLogin() {
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
    }

    @Test
    void testAdminAddProduct() throws InterruptedException {
        // Test Login for Admin
        driver.get("http://localhost:" + port + "/login");
        assertEquals("Login", driver.getTitle());

        driver.findElement(By.id("email")).sendKeys("admin@example.com");
        driver.findElement(By.id("password")).sendKeys(adminRawPassword);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Verify redirect to admin home page after successful login
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("/adminHome"));
        assertTrue(driver.getCurrentUrl().contains("/adminHome"));
        assertEquals("Admin Home", driver.getTitle());

        // Navigate to Add Product Page
        driver.get("http://localhost:" + port + "/admin/addProduct");
        assertEquals("Add Product", driver.getTitle());

        // Fill Product Details
        driver.findElement(By.id("prodName")).sendKeys("Test Product");
        driver.findElement(By.id("prodType")).sendKeys("Electronics");
        driver.findElement(By.id("prodInfo")).sendKeys("A product for integration testing.");
        driver.findElement(By.id("prodPrice")).sendKeys("99.99");
        driver.findElement(By.id("prodQuantity")).sendKeys("10");
        // Locate the file input element and upload the dummy image
        File dummyImageFile = new File("src/test/resources/dummy.txt");
        driver.findElement(By.name("prodImage")).sendKeys(dummyImageFile.getAbsolutePath());

        driver.findElement(By.tagName("form")).submit();

        // Verify redirect to View Products page after adding
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("/admin/products/view"));
        assertTrue(driver.getCurrentUrl().contains("/admin/products/view"));
        assertEquals("View Products", driver.getTitle());

                // Verify redirect to View Products page after adding
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("/admin/products/view"));
        assertEquals("View Products", driver.getTitle());

        Thread.sleep(1000);

        // Verify product is in the list
    }
}