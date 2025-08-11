package com.example.shoppingcart.controller;

import com.example.shoppingcart.dataaccess.user.User;
import com.example.shoppingcart.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // This will resolve to register.html
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        // Check if user with email or mobile already exists
        if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("errorMessage", "User with this email already exists.");
            return "register";
        }
        // You might want to add similar check for mobile number

        userService.registerNewUser(user);
        return "redirect:/login"; // Redirect to login page after successful registration
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // This will resolve to login.html
    }

    // Placeholder for actual login handling (will be handled by Spring Security)
    @PostMapping("/login")
    public String loginUser() {
        // Spring Security will handle the actual authentication
        return "redirect:/userHome"; // Redirect to user home page after successful login
    }

    @GetMapping("/userHome")
    public String userHome() {
        return "userHome"; // This will resolve to userHome.html
    }
}