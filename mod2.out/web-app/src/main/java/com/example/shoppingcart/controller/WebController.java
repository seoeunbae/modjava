package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.User;
import com.example.shoppingcart.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebController {

    private final UserService userService;

    public WebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // This will resolve to register.html
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // This will resolve to login.html
    }

    @GetMapping("/userHome")
    public String userHome() {
        return "userHome"; // This will resolve to userHome.html
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        // Check if user with email already exists
        if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("errorMessage", "User with this email already exists.");
            return "register";
        }

        userService.registerUser(user);
        return "redirect:/login"; // Redirect to login page after successful registration
    }
}
