package com.example.shoppingcart.service;

import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProduct() {
        Product product = new Product("P001", "Laptop", "Electronics", "Powerful laptop", 1200.00, 10, null);
        when(productRepository.save(product)).thenReturn(product);

        Product savedProduct = productService.addProduct(product);

        assertNotNull(savedProduct);
        assertEquals("P001", savedProduct.getProdId());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product("P001", "Laptop", "Electronics", "Updated info", 1250.00, 8, null);
        when(productRepository.save(product)).thenReturn(product);

        Product updatedProduct = productService.updateProduct(product);

        assertNotNull(updatedProduct);
        assertEquals("Updated info", updatedProduct.getProdInfo());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testDeleteProduct() {
        String prodId = "P001";
        doNothing().when(productRepository).deleteById(prodId);

        productService.deleteProduct(prodId);

        verify(productRepository, times(1)).deleteById(prodId);
    }

    @Test
    void testGetProductByIdFound() {
        String prodId = "P001";
        Product product = new Product(prodId, "Laptop", "Electronics", "Powerful laptop", 1200.00, 10, null);
        when(productRepository.findById(prodId)).thenReturn(Optional.of(product));

        Optional<Product> foundProduct = productService.getProductById(prodId);

        assertTrue(foundProduct.isPresent());
        assertEquals(prodId, foundProduct.get().getProdId());
    }

    @Test
    void testGetProductByIdNotFound() {
        String prodId = "P002";
        when(productRepository.findById(prodId)).thenReturn(Optional.empty());

        Optional<Product> foundProduct = productService.getProductById(prodId);

        assertFalse(foundProduct.isPresent());
    }

    @Test
    void testGetAllProducts() {
        Product product1 = new Product("P001", "Laptop", "Electronics", "Info1", 1000.00, 5, null);
        Product product2 = new Product("P002", "Mouse", "Electronics", "Info2", 25.00, 50, null);
        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.findAll()).thenReturn(products);

        List<Product> allProducts = productService.getAllProducts();

        assertNotNull(allProducts);
        assertEquals(2, allProducts.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testSearchProductsByName() {
        String searchName = "lap";
        Product product1 = new Product("P001", "Laptop", "Electronics", "Info1", 1000.00, 5, null);
        List<Product> products = Arrays.asList(product1);
        when(productRepository.findByProdNameContainingIgnoreCase(searchName)).thenReturn(products);

        List<Product> foundProducts = productService.searchProductsByName(searchName);

        assertNotNull(foundProducts);
        assertEquals(1, foundProducts.size());
        assertEquals("Laptop", foundProducts.get(0).getProdName());
        verify(productRepository, times(1)).findByProdNameContainingIgnoreCase(searchName);
    }

    @Test
    void testGetProductsByType() {
        String prodType = "Electronics";
        Product product1 = new Product("P001", "Laptop", "Electronics", "Info1", 1000.00, 5, null);
        Product product2 = new Product("P002", "Mouse", "Electronics", "Info2", 25.00, 50, null);
        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.findByProdType(prodType)).thenReturn(products);

        List<Product> foundProducts = productService.getProductsByType(prodType);

        assertNotNull(foundProducts);
        assertEquals(2, foundProducts.size());
        assertEquals("Electronics", foundProducts.get(0).getProdType());
        verify(productRepository, times(1)).findByProdType(prodType);
    }
}
