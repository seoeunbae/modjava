package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.Cart;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.model.Order;
import com.example.shoppingcart.model.User;
import com.example.shoppingcart.service.CartService;
import com.example.shoppingcart.service.OrderService;
import com.example.shoppingcart.service.ProductService;
import com.example.shoppingcart.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class WebController {

    private final UserService userService;
    private final ProductService productService;
    private final CartService cartService;
    private final OrderService orderService;

    public WebController(UserService userService, ProductService productService, CartService cartService, OrderService orderService) {
        this.userService = userService;
        this.productService = productService;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // This will resolve to register.html
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/products";
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
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("errorMessage", "User with this email already exists.");
            return "register";
        }

        userService.registerUser(user);
        return "redirect:/login"; // Redirect to login page after successful registration
    }

    @GetMapping("/products")
    public String products(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/admin/products/view")
    public String viewProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "viewProducts";
    }



    @GetMapping("/user/checkout")
    public String checkout() {
        return "checkout";
    }

    @PostMapping("/user/checkout")
    public String placeOrder(Principal principal, @RequestParam String address, @RequestParam String city, @RequestParam String state, @RequestParam String zip) {
        String userEmail = principal.getName();
        orderService.placeOrder(userEmail, address, city, state, zip);
        return "redirect:/orders";
    }

    

    @GetMapping("/adminHome")
    public String adminHome() {
        return "adminHome";
    }

    @GetMapping("/orders")
    public String viewOrderHistory(Model model, Principal principal) {
        String userEmail = principal.getName();
        List<Order> orders = orderService.getOrdersByUser(userEmail);
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/user/profile")
    public String viewUserProfile(Model model, Principal principal) {
        String userEmail = principal.getName();
        userService.findByEmail(userEmail).ifPresent(user -> model.addAttribute("user", user));
        return "userProfile";
    }

    @PostMapping("/user/profile")
    public String updateUserProfile(@ModelAttribute("user") User user, Principal principal, Model model) {
        String userEmail = principal.getName();
        userService.findByEmail(userEmail).ifPresent(existingUser -> {
            existingUser.setName(user.getName());
            existingUser.setMobile(user.getMobile());
            existingUser.setAddress(user.getAddress());
            existingUser.setPincode(user.getPincode());
            userService.updateUser(existingUser);
            model.addAttribute("successMessage", "Profile updated successfully!");
            model.addAttribute("user", existingUser);
        });
        return "userProfile";
    }
}
