package com.example.shoppingcart.repository;

import com.example.shoppingcart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findByProdId(String prodId);
    List<Product> findByProdNameContainingIgnoreCase(String prodName);
    List<Product> findByProdType(String prodType);
}
