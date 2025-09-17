
package com.shoppingcart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOrderById() {
        // Given
        OrderPK orderPK = new OrderPK("order1", "prod1");
        Order order = new Order();
        when(orderRepository.findById(orderPK)).thenReturn(Optional.of(order));

        // When
        orderServiceImpl.getOrderById(orderPK);

        // Then
        verify(orderRepository).findById(orderPK);
    }

    @Test
    public void testGetOrdersByTransactionId() {
        // Given
        String transactionId = "trans1";

        // When
        orderServiceImpl.getOrdersByTransactionId(transactionId);

        // Then
        verify(orderRepository).findByOrderid(transactionId);
    }

    @Test
    public void testUpdateOrderStatus() {
        // Given
        OrderPK orderPK = new OrderPK("order1", "prod1");
        Order order = new Order();
        when(orderRepository.findById(orderPK)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        // When
        orderServiceImpl.updateOrderStatus(orderPK, 1);

        // Then
        verify(orderRepository).findById(orderPK);
        verify(orderRepository).save(order);
    }

    @Test
    public void testGetAllOrders() {
        // Given
        List<Order> orders = new ArrayList<>();
        when(orderRepository.findAll()).thenReturn(orders);

        // When
        orderServiceImpl.getAllOrders();

        // Then
        verify(orderRepository).findAll();
    }
}
