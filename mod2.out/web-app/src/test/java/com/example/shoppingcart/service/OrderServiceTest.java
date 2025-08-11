package com.example.shoppingcart.service;

import com.example.shoppingcart.dataaccess.cart.Cart;
import com.example.shoppingcart.dataaccess.cart.CartItem;
import com.example.shoppingcart.dataaccess.order.Order;
import com.example.shoppingcart.dataaccess.order.OrderItem;
import com.example.shoppingcart.dataaccess.order.OrderItemRepository;
import com.example.shoppingcart.dataaccess.order.OrderRepository;
import com.example.shoppingcart.dataaccess.product.Product;
import com.example.shoppingcart.dataaccess.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    private User testUser;
    private Product testProduct1;
    private Product testProduct2;
    private Cart testCart;
    private CartItem testCartItem1;
    private CartItem testCartItem2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User("Test User", 1234567890L, "test@example.com", "123 Test St", 12345, "password");
        testUser.setId(1L);

        testProduct1 = new Product("P001", "Laptop", "Electronics", "Powerful laptop", 1200.00, 10, null);
        testProduct2 = new Product("P002", "Mouse", "Electronics", "Wireless mouse", 25.00, 50, null);

        testCart = new Cart(testUser);
        testCart.setId(1L);

        testCartItem1 = new CartItem(testCart, testProduct1, 1);
        testCartItem2 = new CartItem(testCart, testProduct2, 2);

        testCart.addCartItem(testCartItem1);
        testCart.addCartItem(testCartItem2);
    }

    @Test
    void testPlaceOrder() {
        when(userService.findByEmail(testUser.getEmail())).thenReturn(testUser);
        when(cartService.getCartByUser(testUser.getEmail())).thenReturn(testCart);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L);
            return order;
        });
        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productService.updateProduct(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(cartService).clearCart(testUser.getEmail());

        Order placedOrder = orderService.placeOrder(testUser.getEmail());

        assertNotNull(placedOrder);
        assertEquals(testUser.getId(), placedOrder.getUser().getId());
        assertEquals(2, placedOrder.getOrderItems().size());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(2)).save(any(OrderItem.class));
        verify(productService, times(2)).updateProduct(any(Product.class));
        verify(cartService, times(1)).clearCart(testUser.getEmail());
    }

    @Test
    void testGetOrdersByUser() {
        Order order1 = new Order(testUser, LocalDateTime.now(), 100.0, 0);
        Order order2 = new Order(testUser, LocalDateTime.now(), 200.0, 1);
        List<Order> orders = Arrays.asList(order1, order2);

        when(userService.findByEmail(testUser.getEmail())).thenReturn(testUser);
        when(orderRepository.findByUser(testUser)).thenReturn(orders);

        List<Order> resultOrders = orderService.getOrdersByUser(testUser.getEmail());

        assertNotNull(resultOrders);
        assertEquals(2, resultOrders.size());
        verify(orderRepository, times(1)).findByUser(testUser);
    }

    @Test
    void testGetOrderByIdFound() {
        Order order = new Order(testUser, LocalDateTime.now(), 100.0, 0);
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> resultOrder = orderService.getOrderById(1L);

        assertTrue(resultOrder.isPresent());
        assertEquals(1L, resultOrder.get().getId());
    }

    @Test
    void testGetOrderByIdNotFound() {
        when(orderRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Order> resultOrder = orderService.getOrderById(2L);

        assertFalse(resultOrder.isPresent());
    }

    @Test
    void testUpdateOrderStatus() {
        Order order = new Order(testUser, LocalDateTime.now(), 100.0, 0);
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order updatedOrder = orderService.updateOrderStatus(1L, 1);

        assertNotNull(updatedOrder);
        assertEquals(1, updatedOrder.getShipped());
        verify(orderRepository, times(1)).save(order);
    }
}
