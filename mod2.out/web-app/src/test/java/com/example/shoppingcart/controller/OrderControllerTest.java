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

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void viewOrderDetails() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(Optional.of(new Order()));

        mockMvc.perform(get("/orders/{orderId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("orderDetails"));
    }

    @Test
    void viewOrderDetailsNotFound() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/orders/{orderId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    void updateOrderStatus() throws Exception {
        mockMvc.perform(post("/admin/orders/updateStatus")
                        .param("orderId", "1")
                        .param("shipped", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/orders/unshipped"));
    }

    @Test
    void viewShippedOrders() throws Exception {
        when(orderService.getAllShippedOrders()).thenReturn(Arrays.asList(new Order(), new Order()));

        mockMvc.perform(get("/admin/orders/shipped"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminShippedOrders"));
    }

    @Test
    void viewUnshippedOrders() throws Exception {
        when(orderService.getAllUnshippedOrders()).thenReturn(Arrays.asList(new Order(), new Order()));

        mockMvc.perform(get("/admin/orders/unshipped"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminUnshippedOrders"));
    }
}