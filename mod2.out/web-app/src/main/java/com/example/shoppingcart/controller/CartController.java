package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.Cart;
import com.example.shoppingcart.service.CartService;
import com.example.shoppingcart.service.ProductService;
import com.example.shoppingcart.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/user/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;

    public CartController(CartService cartService, ProductService productService, UserService userService) {
        this.cartService = cartService;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping
    public String viewCart(Model model, Principal principal) {
        String userEmail = principal.getName();
        Cart cart = cartService.getCartByUser(userEmail);
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/add")
    public String addItemToCart(@RequestParam("productId") String prodId, @RequestParam("quantity") int quantity, Principal principal) {
        String userEmail = principal.getName();
        cartService.addItemToCart(userEmail, prodId, quantity);
        return "redirect:/user/cart";
    }

    @PostMapping("/update")
    public String updateCartItemQuantity(@RequestParam("prodId") String prodId, @RequestParam("quantity") int quantity, Principal principal) {
        String userEmail = principal.getName();
        cartService.updateCartItemQuantity(userEmail, prodId, quantity);
        return "redirect:/user/cart";
    }

    @PostMapping("/remove")
    public String removeCartItem(@RequestParam("prodId") String prodId, Principal principal) {
        String userEmail = principal.getName();
        cartService.removeCartItem(userEmail, prodId);
        return "redirect:/user/cart";
    }
}
