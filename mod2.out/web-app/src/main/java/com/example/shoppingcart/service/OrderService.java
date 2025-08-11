package com.example.shoppingcart.service;

import com.example.shoppingcart.model.Cart;
import com.example.shoppingcart.model.CartItem;
import com.example.shoppingcart.model.Order;
import com.example.shoppingcart.model.OrderItem;
import com.example.shoppingcart.repository.OrderItemRepository;
import com.example.shoppingcart.repository.OrderRepository;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.model.User;
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

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartService cartService, UserService userService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.userService = userService;
        this.productService = productService;
    }

    @Transactional
    public Order placeOrder(String userEmail) {
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

        Order order = new Order(user, LocalDateTime.now(), totalAmount, 0); // 0 for not shipped
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

        return order;
    }

    public List<Order> getOrdersByUser(String userEmail) {
        User user = userService.findByEmail(userEmail);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return orderRepository.findByUser(user);
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, int shippedStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setShipped(shippedStatus);
        return orderRepository.save(order);
    }

    public List<Order> getAllShippedOrders() {
        return orderRepository.findByShipped(1);
    }

    public List<Order> getAllUnshippedOrders() {
        return orderRepository.findByShipped(0);
    }
}
