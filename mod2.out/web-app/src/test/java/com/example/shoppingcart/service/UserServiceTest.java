package com.example.shoppingcart.service;

import com.example.shoppingcart.dataaccess.user.User;
import com.example.shoppingcart.dataaccess.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterNewUser() {
        User user = new User("Test User", 1234567890L, "test@example.com", "123 Test St", 12345, "password");
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.registerNewUser(user);

        assertNotNull(registeredUser);
        assertEquals("encodedPassword", registeredUser.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindByEmailFound() {
        User user = new User("Test User", 1234567890L, "test@example.com", "123 Test St", 12345, "encodedPassword");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        User foundUser = userService.findByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
    }

    @Test
    void testFindByEmailNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        User foundUser = userService.findByEmail("nonexistent@example.com");

        assertNull(foundUser);
    }

    @Test
    void testAuthenticateUserSuccess() {
        User user = new User("Test User", 1234567890L, "test@example.com", "123 Test St", 12345, "encodedPassword");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        boolean isAuthenticated = userService.authenticateUser("test@example.com", "password");

        assertTrue(isAuthenticated);
    }

    @Test
    void testAuthenticateUserFailureWrongPassword() {
        User user = new User("Test User", 1234567890L, "test@example.com", "123 Test St", 12345, "encodedPassword");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        boolean isAuthenticated = userService.authenticateUser("test@example.com", "wrongPassword");

        assertFalse(isAuthenticated);
    }

    @Test
    void testAuthenticateUserFailureUserNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        boolean isAuthenticated = userService.authenticateUser("nonexistent@example.com", "password");

        assertFalse(isAuthenticated);
    }
}
