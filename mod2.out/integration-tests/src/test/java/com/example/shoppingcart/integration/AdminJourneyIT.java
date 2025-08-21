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
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(classes = ShoppingCartApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ContextConfiguration(initializers = TestcontainersInitializer.class)
public class AdminJourneyIT {

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

    private String adminRawPassword = "adminpass";

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

        // Create an admin user for testing
        User adminUser = new User("Admin User", "admin@example.com", passwordEncoder.encode(adminRawPassword), "1234567890", "Admin Address", "12345", "ADMIN");
        userRepository.save(adminUser);

        // Create products for testing
        Product productToUpdate = new Product("prod-update", "Product to Update", "Electronics", "Info", 10.0, 10, null);
        productRepository.save(productToUpdate);

        Product productToDelete = new Product("prod-delete", "Product to Delete", "Books", "Info", 20.0, 5, null);
        productRepository.save(productToDelete);
    }

    @AfterAll
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void loginAsAdmin() {
        driver.get("http://localhost:" + port + "/login");
        driver.findElement(By.id("email")).sendKeys("admin@example.com");
        driver.findElement(By.id("password")).sendKeys(adminRawPassword);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("/adminHome"));
    }

    @Test
    void testUpdateProduct() {
        loginAsAdmin();

        // Navigate to the view products page
        driver.get("http://localhost:" + port + "/admin/products/view");
        assertEquals("View Products", driver.getTitle());

        // Click the update link for the product
        driver.findElement(By.xpath("//a[@href='/admin/products/update/prod-update']")).click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.titleIs("Update Product"));

        // Update the product details
        driver.findElement(By.id("prodName")).clear();
        driver.findElement(By.id("prodName")).sendKeys("Updated Product Name");
        driver.findElement(By.id("prodPrice")).clear();
        driver.findElement(By.id("prodPrice")).sendKeys("15.0");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Verify that the product is updated
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.titleIs("View Products"));
        assertTrue(driver.getPageSource().contains("Updated Product Name"));
        assertTrue(driver.getPageSource().contains("15.0"));
    }

    @Test
    void testDeleteProduct() {
        loginAsAdmin();

        // Navigate to the view products page
        driver.get("http://localhost:" + port + "/admin/products/view");
        assertEquals("View Products", driver.getTitle());

        // Click the delete link for the product
        driver.findElement(By.xpath("//a[@href='/admin/products/delete/prod-delete']")).click();
        
        // Verify that the product is deleted
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.titleIs("View Products"));
        assertFalse(driver.getPageSource().contains("Product to Delete"));
    }
}