package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.Cart;
import com.example.shoppingcart.model.CartItem;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.model.User;
import com.example.shoppingcart.service.CartService;
import com.example.shoppingcart.service.ProductService;
import com.example.shoppingcart.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/cart")
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
    public ResponseEntity<Cart> viewCart(Principal principal) {
        String userEmail = principal.getName();
        Cart cart = cartService.getCartByUser(userEmail);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addItemToCart(@RequestParam("prodId") String prodId, @RequestParam("quantity") int quantity, Principal principal) {
        String userEmail = principal.getName();
        cartService.addItemToCart(userEmail, prodId, quantity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateCartItemQuantity(@RequestParam("prodId") String prodId, @RequestParam("quantity") int quantity, Principal principal) {
        String userEmail = principal.getName();
        cartService.updateCartItemQuantity(userEmail, prodId, quantity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/remove")
    public ResponseEntity<Void> removeCartItem(@RequestParam("prodId") String prodId, Principal principal) {
        String userEmail = principal.getName();
        cartService.removeCartItem(userEmail, prodId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/clear")
    public ResponseEntity<Void> clearCart(Principal principal) {
        String userEmail = principal.getName();
        cartService.clearCart(userEmail);
        return ResponseEntity.ok().build();
    }
}
