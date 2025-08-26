
package com.example.shoppingcart.integration;

import com.example.shoppingcart.ShoppingCartApplication;
import com.example.shoppingcart.model.User;
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

@SpringBootTest(classes = ShoppingCartApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ContextConfiguration(initializers = TestcontainersInitializer.class)
public class UserProfileJourneyIT {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

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
        User regularUser = new User("Regular User", "userprofile@example.com", passwordEncoder.encode(userRawPassword), "0987654321", "User Address", "54321", "USER");
        userRepository.save(regularUser);
    }

    @AfterAll
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void loginAsUser() {
        driver.get("http://localhost:" + port + "/login");
        driver.findElement(By.id("email")).sendKeys("userprofile@example.com");
        driver.findElement(By.id("password")).sendKeys(userRawPassword);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("/userHome"));
    }

    @Test
    void testViewAndUpdateUserProfile() {
        loginAsUser();

        // Navigate to the user profile page
        driver.get("http://localhost:" + port + "/user/profile");
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.titleIs("User Profile"));

        // Verify current user information
        assertEquals("Regular User", driver.findElement(By.id("name")).getAttribute("value"));
        assertEquals("userprofile@example.com", driver.findElement(By.id("email")).getAttribute("value"));
        assertEquals("0987654321", driver.findElement(By.id("mobile")).getAttribute("value"));
        assertEquals("User Address", driver.findElement(By.id("address")).getAttribute("value"));

        // Update user information
        driver.findElement(By.id("name")).clear();
        driver.findElement(By.id("name")).sendKeys("Updated User Name");
        driver.findElement(By.id("address")).clear();
        driver.findElement(By.id("address")).sendKeys("Updated User Address");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Verify that the profile is updated
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.titleIs("User Profile"));
        assertTrue(driver.getPageSource().contains("Profile updated successfully!"));
        assertEquals("Updated User Name", driver.findElement(By.id("name")).getAttribute("value"));
        assertEquals("Updated User Address", driver.findElement(By.id("address")).getAttribute("value"));
    }
}
