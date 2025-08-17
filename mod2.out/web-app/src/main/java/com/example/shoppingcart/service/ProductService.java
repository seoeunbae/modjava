package com.example.shoppingcart.service;

import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public boolean addProduct(String prodName, String prodType, String prodInfo, double prodPrice, int prodQuantity, byte[] prodImage) {
        Product product = new Product();
        product.setProdName(prodName);
        product.setProdType(prodType);
        product.setProdInfo(prodInfo);
        product.setProdPrice(prodPrice);
        product.setProdQuantity(prodQuantity);
        product.setProdImage(prodImage);

        // Generate a unique product ID (e.g., UUID or sequence)
        // For simplicity, let's use a random UUID for now.
        product.setProdId(java.util.UUID.randomUUID().toString());

        try {
            productRepository.save(product);
            System.out.println("Product saved successfully: " + product.getProdId());
            return true;
        } catch (Exception e) {
            System.err.println("Error saving product: " + e.getMessage());
            e.printStackTrace();
            return false; // Indicate failure
        }
    }

    public Product updateProduct(Product product) {
        // Add any business logic or validation before saving
        // Ensure the product exists before updating
        if (productRepository.existsById(product.getProdId())) {
            return productRepository.save(product);
        } else {
            // Handle case where product does not exist
            throw new RuntimeException("Product with ID " + product.getProdId() + " not found.");
        }
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
}