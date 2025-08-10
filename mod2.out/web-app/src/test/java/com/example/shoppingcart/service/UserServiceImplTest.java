package com.example.shoppingcart.service;

import com.example.shoppingcart.model.User;
import com.example.shoppingcart.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    @Test
    public void testRegisterUser() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        UserServiceImpl userService = new UserServiceImpl(userRepository, passwordEncoder);

        User user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setPassword("password");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User registeredUser = userService.registerUser(user);

        assertEquals("test@example.com", registeredUser.getEmail());
        assertEquals("Test User", registeredUser.getName());
        assertEquals("encodedPassword", registeredUser.getPassword());
    }
}
