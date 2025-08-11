package com.example.shoppingcart.dataaccess.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByProdNameContainingIgnoreCase(String prodName);
    List<Product> findByProdType(String prodType);
}
