package com.example.shoppingcart.service;

import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(String prodId) {
        productRepository.deleteById(prodId);
    }

    public Optional<Product> getProductById(String prodId) {
        return productRepository.findById(prodId);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchProductsByName(String prodName) {
        return productRepository.findByProdNameContainingIgnoreCase(prodName);
    }

    public List<Product> getProductsByType(String prodType) {
        return productRepository.findByProdType(prodType);
    }
}
