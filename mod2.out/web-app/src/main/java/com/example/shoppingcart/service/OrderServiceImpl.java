package com.example.shoppingcart.service;

import com.example.shoppingcart.model.*;
import com.example.shoppingcart.repository.CartRepository;
import com.example.shoppingcart.repository.OrderRepository;
import com.example.shoppingcart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CartRepository cartRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Order placeOrder(String userEmail) {
        Cart cart = cartRepository.findByUserEmail(userEmail);
        if (cart != null && !cart.getItems().isEmpty()) {
            User user = userRepository.findByEmail(userEmail);
            Order order = new Order();
            order.setUser(user);
            order.setOrderDate(LocalDateTime.now());
            order.setShipped(false);

            List<OrderItem> orderItems = new ArrayList<>();
            double totalAmount = 0.0;

            for (CartItem cartItem : cart.getItems()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setOrder(order);
                orderItems.add(orderItem);
                totalAmount += cartItem.getProduct().getPrice() * cartItem.getQuantity();
            }

            order.setItems(orderItems);
            order.setTotalAmount(totalAmount);

            cart.getItems().clear();
            cartRepository.save(cart);

            return orderRepository.save(order);
        }
        return null;
    }

    @Override
    public List<Order> getOrdersByUserEmail(String userEmail) {
        return orderRepository.findByUserEmail(userEmail);
    }
}
