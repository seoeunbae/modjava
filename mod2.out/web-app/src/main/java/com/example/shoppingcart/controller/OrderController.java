package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.Order;
import com.example.shoppingcart.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{userEmail}")
    public Order placeOrder(@PathVariable String userEmail) {
        return orderService.placeOrder(userEmail);
    }

    @GetMapping("/{userEmail}")
    public List<Order> getOrders(@PathVariable String userEmail) {
        return orderService.getOrdersByUserEmail(userEmail);
    }
}
