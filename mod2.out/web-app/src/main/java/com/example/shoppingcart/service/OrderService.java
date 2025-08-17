package com.example.shoppingcart.service;

import com.example.shoppingcart.model.Cart;
import com.example.shoppingcart.model.CartItem;
import com.example.shoppingcart.model.Order;
import com.example.shoppingcart.model.OrderItem;
import com.example.shoppingcart.repository.OrderItemRepository;
import com.example.shoppingcart.repository.OrderRepository;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.model.User;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final UserService userService;
    private final ProductService productService;
    private final EmailService emailService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartService cartService, UserService userService, ProductService productService, EmailService emailService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.userService = userService;
        this.productService = productService;
        this.emailService = emailService;
    }

    @Transactional
    public Order placeOrder(String userEmail, String shippingAddress, String city, String state, String zip) {
        User user = userService.findByEmail(userEmail);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Cart cart = cartService.getCartByUser(userEmail);
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        double totalAmount = cart.getCartItems().stream()
                .mapToDouble(item -> item.getProduct().getProdPrice() * item.getQuantity())
                .sum();

        Order order = new Order(user, LocalDateTime.now(), totalAmount, 0, shippingAddress, city, state, zip); // 0 for not shipped
        order = orderRepository.save(order);

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem(order, cartItem.getProduct(), cartItem.getQuantity(), cartItem.getProduct().getProdPrice());
            order.addOrderItem(orderItem);
            orderItemRepository.save(orderItem);

            // Update product quantity
            Product product = cartItem.getProduct();
            product.setProdQuantity(product.getProdQuantity() - cartItem.getQuantity());
            productService.updateProduct(product);
        }

        cartService.clearCart(userEmail);

        emailService.sendOrderConfirmationEmail(order);

        return order;
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByUser(String userEmail) {
        User user = userService.findByEmail(userEmail);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        List<Order> orders = orderRepository.findByUser(user);
        orders.forEach(order -> {
            Hibernate.initialize(order.getOrderItems());
            order.getOrderItems().forEach(item -> Hibernate.initialize(item.getProduct()));
        });
        return orders;
    }

    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        order.ifPresent(o -> {
            Hibernate.initialize(o.getOrderItems());
            o.getOrderItems().forEach(item -> Hibernate.initialize(item.getProduct()));
        });
        return order;
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, int shippedStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setShipped(shippedStatus);
        Order updatedOrder = orderRepository.save(order);
        if (shippedStatus == 1) {
            emailService.sendShippingUpdateEmail(updatedOrder);
        }
        return updatedOrder;
    }

    @Transactional(readOnly = true)
    public List<Order> getAllShippedOrders() {
        List<Order> orders = orderRepository.findByShipped(1);
        orders.forEach(order -> {
            Hibernate.initialize(order.getOrderItems());
            order.getOrderItems().forEach(item -> Hibernate.initialize(item.getProduct()));
        });
        return orders;
    }

    @Transactional(readOnly = true)
    public List<Order> getAllUnshippedOrders() {
        List<Order> orders = orderRepository.findByShipped(0);
        orders.forEach(order -> {
            Hibernate.initialize(order.getOrderItems());
            order.getOrderItems().forEach(item -> Hibernate.initialize(item.getProduct()));
        });
        return orders;
    }
}
