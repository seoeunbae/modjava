package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.Cart;
import com.example.shoppingcart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userEmail}")
    public Cart getCart(@PathVariable String userEmail) {
        return cartService.getCartByUserEmail(userEmail);
    }

    @PostMapping("/{userEmail}/items")
    public Cart addItemToCart(@PathVariable String userEmail, @RequestParam String productId, @RequestParam int quantity) {
        return cartService.addItemToCart(userEmail, productId, quantity);
    }

    @PutMapping("/{userEmail}/items")
    public Cart updateItemInCart(@PathVariable String userEmail, @RequestParam String productId, @RequestParam int quantity) {
        return cartService.updateItemInCart(userEmail, productId, quantity);
    }

    @DeleteMapping("/{userEmail}/items/{productId}")
    public Cart removeItemFromCart(@PathVariable String userEmail, @PathVariable String productId) {
        return cartService.removeItemFromCart(userEmail, productId);
    }
}
