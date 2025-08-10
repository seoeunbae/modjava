package com.example.shoppingcart.service;

import com.example.shoppingcart.model.Cart;
import com.example.shoppingcart.model.CartItem;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.model.User;
import com.example.shoppingcart.repository.CartRepository;
import com.example.shoppingcart.repository.ProductRepository;
import com.example.shoppingcart.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartServiceImplTest {

    @Test
    public void testAddItemToCart_newCart() {
        CartRepository cartRepository = mock(CartRepository.class);
        ProductRepository productRepository = mock(ProductRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        CartService cartService = new CartServiceImpl(cartRepository, productRepository, userRepository);

        User user = new User();
        user.setEmail("test@example.com");

        Product product = new Product();
        product.setId("123");
        product.setName("Test Product");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(productRepository.findById("123")).thenReturn(Optional.of(product));
        when(cartRepository.findByUserEmail("test@example.com")).thenReturn(null);
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArguments()[0]);

        Cart cart = cartService.addItemToCart("test@example.com", "123", 1);

        assertEquals(1, cart.getItems().size());
        assertEquals("123", cart.getItems().get(0).getProduct().getId());
    }

    @Test
    public void testAddItemToCart_existingCart() {
        CartRepository cartRepository = mock(CartRepository.class);
        ProductRepository productRepository = mock(ProductRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        CartService cartService = new CartServiceImpl(cartRepository, productRepository, userRepository);

        User user = new User();
        user.setEmail("test@example.com");

        Product product = new Product();
        product.setId("123");
        product.setName("Test Product");

        Cart existingCart = new Cart();
        existingCart.setUser(user);
        existingCart.setItems(new ArrayList<>());

        when(productRepository.findById("123")).thenReturn(Optional.of(product));
        when(cartRepository.findByUserEmail("test@example.com")).thenReturn(existingCart);
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArguments()[0]);

        Cart cart = cartService.addItemToCart("test@example.com", "123", 1);

        assertEquals(1, cart.getItems().size());
        assertEquals("123", cart.getItems().get(0).getProduct().getId());
    }
}