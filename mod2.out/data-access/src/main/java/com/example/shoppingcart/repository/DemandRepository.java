package com.example.shoppingcart.repository;

import com.example.shoppingcart.model.Demand;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandRepository extends JpaRepository<Demand, Long> {
    List<Demand> findByProduct(Product product);

    void deleteByUserAndProduct(User user, Product product);
}
