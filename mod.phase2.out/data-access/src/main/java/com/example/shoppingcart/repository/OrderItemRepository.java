package com.example.shoppingcart.repository;

import com.example.shoppingcart.model.Order;
import com.example.shoppingcart.model.OrderItem;
import com.example.shoppingcart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
