package com.example.shoppingcart.service;

import com.example.shoppingcart.model.Cart;
import com.example.shoppingcart.model.CartItem;
import com.example.shoppingcart.repository.CartItemRepository;
import com.example.shoppingcart.repository.CartRepository;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.model.User;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final UserService userService;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductService productService, UserService userService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
        this.userService = userService;
    }

    @Transactional
    public Cart addItemToCart(String userEmail, String prodId, int quantity) {
        User user = userService.findByEmail(userEmail);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Product product = productService.getProductById(prodId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Optional<Cart> optionalCart = cartRepository.findByUser(user);
        Cart cart = optionalCart.orElseGet(() -> cartRepository.save(new Cart(user)));

        Optional<CartItem> optionalCartItem = cartItemRepository.findByCartAndProduct(cart, product);
        CartItem cartItem;
        if (optionalCartItem.isPresent()) {
            cartItem = optionalCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem(cart, product, quantity);
            cart.addCartItem(cartItem);
        }
        cartItemRepository.save(cartItem);
        return cart;
    }

    @Transactional
    public Cart updateCartItemQuantity(String userEmail, String prodId, int quantity) {
        User user = userService.findByEmail(userEmail);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Product product = productService.getProductById(prodId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user"));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new IllegalArgumentException("Product not in cart"));

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
            cart.removeCartItem(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
        return cart;
    }

    @Transactional
    public void removeCartItem(String userEmail, String prodId) {
        User user = userService.findByEmail(userEmail);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Product product = productService.getProductById(prodId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user"));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new IllegalArgumentException("Product not in cart"));

        cartItemRepository.delete(cartItem);
        cart.removeCartItem(cartItem);
    }

    @Transactional(readOnly = true)
    public Cart getCartByUser(String userEmail) {
        User user = userService.findByEmail(userEmail);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> createCart(user));
        if (cart != null) {
            Hibernate.initialize(cart.getCartItems());
            cart.getCartItems().forEach(item -> Hibernate.initialize(item.getProduct()));
        }
        return cart;
    }

    @Transactional
    public void clearCart(String userEmail) {
        User user = userService.findByEmail(userEmail);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        Optional<Cart> optionalCart = cartRepository.findByUser(user);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            cartItemRepository.deleteAll(cart.getCartItems());
            cart.getCartItems().clear();
            cartRepository.save(cart);
        }
    }

    @Transactional
    public Cart createCart(User user) {
        Cart cart = new Cart(user);
        return cartRepository.save(cart);
    }
}
