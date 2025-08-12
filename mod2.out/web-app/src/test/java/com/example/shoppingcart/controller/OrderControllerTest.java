package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.Order;
import com.example.shoppingcart.model.User;
import com.example.shoppingcart.service.OrderService;
import com.example.shoppingcart.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private OrderController orderController;

    private User mockUser;
    private Principal mockPrincipal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");

        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(mockUser.getEmail())
                .password("password")
                .roles("USER")
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        mockPrincipal = new Principal() {
            @Override
            public String getName() {
                return mockUser.getEmail();
            }
        };

        when(userService.findByEmail(mockUser.getEmail())).thenReturn(mockUser);
    }

    @Test
    void viewOrders() throws Exception {
        List<Order> orders = Arrays.asList(new Order(), new Order());

        when(orderService.getOrdersByUser(mockUser.getEmail())).thenReturn(orders);

        mockMvc.perform(get("/orders/history").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(view().name("orderHistory"))
                .andExpect(model().attributeExists("orders"));

        verify(orderService, times(1)).getOrdersByUser(mockUser.getEmail());
    }

    @Test
    void placeOrder() throws Exception {
        when(orderService.placeOrder(mockUser.getEmail())).thenReturn(new Order());

        mockMvc.perform(post("/orders/place").principal(mockPrincipal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/history"));

        verify(orderService, times(1)).placeOrder(mockUser.getEmail());
    }

    @Test
    void viewAllOrders() throws Exception {
        List<Order> shippedOrders = Arrays.asList(new Order(), new Order());
        List<Order> unshippedOrders = Arrays.asList(new Order(), new Order());
        List<Order> allOrders = new ArrayList<>();
        allOrders.addAll(shippedOrders);
        allOrders.addAll(unshippedOrders);

        when(orderService.getAllShippedOrders()).thenReturn(shippedOrders);
        when(orderService.getAllUnshippedOrders()).thenReturn(unshippedOrders);

        mockMvc.perform(get("/orders/admin/shipped").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(view().name("adminShippedOrders"))
                .andExpect(model().attributeExists("orders"));

        mockMvc.perform(get("/orders/admin/unshipped").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(view().name("adminUnshippedOrders"))
                .andExpect(model().attributeExists("orders"));

        verify(orderService, times(1)).getAllShippedOrders();
        verify(orderService, times(1)).getAllUnshippedOrders();
    }

    @Test
    void updateOrderStatus() throws Exception {
        when(orderService.updateOrderStatus(anyLong(), anyInt())).thenReturn(new Order());

        mockMvc.perform(post("/orders/admin/orders/updateStatus").principal(mockPrincipal)
                        .param("orderId", "1")
                        .param("shipped", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/orders/unshipped"));

                verify(orderService, times(1)).updateOrderStatus(eq(1L), eq(1));
    }

    @Test
    void viewOrderDetails() throws Exception {
        Order order = new Order();
        order.setId(1L);
        when(orderService.getOrderById(anyLong())).thenReturn(Optional.of(order));

        mockMvc.perform(get("/orders/{orderId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("orderDetails"))
                .andExpect(model().attributeExists("order"));

        verify(orderService, times(1)).getOrderById(eq(1L));
    }

    @Test
    void viewOrderDetailsNotFound() throws Exception {
        when(orderService.getOrderById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/orders/{orderId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));

        verify(orderService, times(1)).getOrderById(eq(1L));
    }
}