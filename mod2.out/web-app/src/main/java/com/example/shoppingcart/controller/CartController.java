package com.example.shoppingcart.controller;

import com.example.shoppingcart.dataaccess.cart.Cart;
import com.example.shoppingcart.dataaccess.cart.CartItem;
import com.example.shoppingcart.dataaccess.product.Product;
import com.example.shoppingcart.dataaccess.user.User;
import com.example.shoppingcart.service.CartService;
import com.example.shoppingcart.service.ProductService;
import com.example.shoppingcart.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/cart")
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
        return "cartDetails";
    }

    @PostMapping("/add")
    public String addItemToCart(@RequestParam("prodId") String prodId, @RequestParam("quantity") int quantity, Principal principal) {
        String userEmail = principal.getName();
        cartService.addItemToCart(userEmail, prodId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCartItemQuantity(@RequestParam("prodId") String prodId, @RequestParam("quantity") int quantity, Principal principal) {
        String userEmail = principal.getName();
        cartService.updateCartItemQuantity(userEmail, prodId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeCartItem(@RequestParam("prodId") String prodId, Principal principal) {
        String userEmail = principal.getName();
        cartService.removeCartItem(userEmail, prodId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(Principal principal) {
        String userEmail = principal.getName();
        cartService.clearCart(userEmail);
        return "redirect:/cart";
    }
}
