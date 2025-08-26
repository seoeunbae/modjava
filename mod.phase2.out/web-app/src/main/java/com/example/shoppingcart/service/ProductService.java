package com.example.shoppingcart.service;

import com.example.shoppingcart.model.Demand;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final DemandService demandService;
    private final EmailService emailService;

    @Autowired
    public ProductService(ProductRepository productRepository, DemandService demandService, EmailService emailService) {
        this.productRepository = productRepository;
        this.demandService = demandService;
        this.emailService = emailService;
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

    @Transactional
    public Product updateProduct(Product product) {
        // Ensure the product exists before updating
        if (productRepository.existsById(product.getProdId())) {
            Product updatedProduct = productRepository.save(product);

            // Check if the product is back in stock
            if (updatedProduct.getProdQuantity() > 0) {
                List<Demand> demands = demandService.getDemandsByProduct(updatedProduct);
                for (Demand demand : demands) {
                    emailService.sendProductAvailableEmail(demand.getUser(), updatedProduct);
                    demandService.deleteDemand(demand.getUser(), updatedProduct);
                }
            }

            return updatedProduct;
        } else {
            // Handle case where product does not exist
            throw new RuntimeException("Product with ID " + product.getProdId() + " not found.");
        }
    }

    public void deleteProduct(String prodId) {
        productRepository.deleteById(prodId);
    }

    @Transactional(readOnly = true)
    public Optional<Product> getProductById(String prodId) {
        return productRepository.findById(prodId);
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
