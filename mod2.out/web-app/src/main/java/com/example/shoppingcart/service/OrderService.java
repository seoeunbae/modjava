package com.example.shoppingcart.service;

import com.example.shoppingcart.model.Order;

import java.util.List;

public interface OrderService {

    Order placeOrder(String userEmail);

    List<Order> getOrdersByUserEmail(String userEmail);
}
