package com.example.shoppingcart.service;

import com.example.shoppingcart.model.*;
import com.example.shoppingcart.repository.CartRepository;
import com.example.shoppingcart.repository.OrderRepository;
import com.example.shoppingcart.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderServiceImplTest {

    @Test
    public void testPlaceOrder() {
        OrderRepository orderRepository = mock(OrderRepository.class);
        CartRepository cartRepository = mock(CartRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        OrderService orderService = new OrderServiceImpl(orderRepository, cartRepository, userRepository);

        User user = new User();
        user.setEmail("test@example.com");

        Product product = new Product();
        product.setId("123");
        product.setName("Test Product");
        product.setPrice(10.0);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(cartItems);

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(cartRepository.findByUserEmail("test@example.com")).thenReturn(cart);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        Order order = orderService.placeOrder("test@example.com");

        assertNotNull(order);
        assertEquals(1, order.getItems().size());
        assertEquals(20.0, order.getTotalAmount());
        assertEquals(0, cart.getItems().size());
    }
}
