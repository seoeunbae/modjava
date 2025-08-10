package com.example.shoppingcart.service;

import com.example.shoppingcart.model.Product;

import java.util.List;

public interface ProductService {

    Product addProduct(Product product);

    Product updateProduct(Product product);

    void deleteProduct(String productId);

    List<Product> getAllProducts();

    Product getProductById(String productId);
}
