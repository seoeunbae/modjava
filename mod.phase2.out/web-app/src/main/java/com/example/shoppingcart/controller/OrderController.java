package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.Order;
import com.example.shoppingcart.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders/{orderId}")
    public String viewOrderDetails(@PathVariable Long orderId, Model model) {
        Optional<Order> order = orderService.getOrderById(orderId);
        if (order.isPresent()) {
            model.addAttribute("order", order.get());
            return "orderDetails";
        } else {
            return "error"; // Or a custom not found page
        }
    }

    // Admin functionality to update order status
    @PostMapping("/admin/orders/updateStatus")
    public String updateOrderStatus(@RequestParam("orderId") Long orderId, @RequestParam("shipped") int shipped) {
        orderService.updateOrderStatus(orderId, shipped);
        return "redirect:/admin/orders/shipped"; // Redirect to unshipped orders view after update
    }

    @GetMapping("/admin/orders/shipped")
    public String viewShippedOrders(Model model) {
        List<Order> orders = orderService.getAllShippedOrders();
        model.addAttribute("orders", orders);
        return "adminShippedOrders";
    }

    @GetMapping("/admin/orders/unshipped")
    public String viewUnshippedOrders(Model model) {
        List<Order> orders = orderService.getAllUnshippedOrders();
        model.addAttribute("orders", orders);
        return "adminUnshippedOrders";
    }
}
