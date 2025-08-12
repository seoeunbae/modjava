package com.example.shoppingcart.service;

import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product("P001", "Laptop", "Electronics", "Powerful laptop", 1200.00, 10, null);
        product2 = new Product("P002", "Mouse", "Electronics", "Wireless mouse", 25.00, 50, null);
    }

    @Test
    void addProduct_shouldReturnSavedProduct() {
        when(productRepository.save(product1)).thenReturn(product1);

        Product savedProduct = productService.addProduct(product1);

        assertNotNull(savedProduct);
        assertEquals(product1.getProdId(), savedProduct.getProdId());
        verify(productRepository, times(1)).save(product1);
    }

    @Test
    void updateProduct_shouldReturnUpdatedProduct_whenProductExists() {
        when(productRepository.existsById(product1.getProdId())).thenReturn(true);
        when(productRepository.save(product1)).thenReturn(product1);

        Product updatedProduct = productService.updateProduct(product1);

        assertNotNull(updatedProduct);
        assertEquals(product1.getProdId(), updatedProduct.getProdId());
        verify(productRepository, times(1)).existsById(product1.getProdId());
        verify(productRepository, times(1)).save(product1);
    }

    @Test
    void updateProduct_shouldThrowRuntimeException_whenProductDoesNotExist() {
        when(productRepository.existsById(product1.getProdId())).thenReturn(false);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            productService.updateProduct(product1);
        });

        assertTrue(thrown.getMessage().contains("not found"));
        verify(productRepository, times(1)).existsById(product1.getProdId());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_shouldCallDeleteById() {
        doNothing().when(productRepository).deleteById(product1.getProdId());

        productService.deleteProduct(product1.getProdId());

        verify(productRepository, times(1)).deleteById(product1.getProdId());
    }

    @Test
    void getProductById_shouldReturnProduct_whenProductExists() {
        when(productRepository.findById(product1.getProdId())).thenReturn(Optional.of(product1));

        Optional<Product> foundProduct = productService.getProductById(product1.getProdId());

        assertTrue(foundProduct.isPresent());
        assertEquals(product1.getProdId(), foundProduct.get().getProdId());
        verify(productRepository, times(1)).findById(product1.getProdId());
    }

    @Test
    void getProductById_shouldReturnEmptyOptional_whenProductDoesNotExist() {
        when(productRepository.findById(product1.getProdId())).thenReturn(Optional.empty());

        Optional<Product> foundProduct = productService.getProductById(product1.getProdId());

        assertFalse(foundProduct.isPresent());
        verify(productRepository, times(1)).findById(product1.getProdId());
    }

    @Test
    void getAllProducts_shouldReturnListOfProducts() {
        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.findAll()).thenReturn(products);

        List<Product> foundProducts = productService.getAllProducts();

        assertNotNull(foundProducts);
        assertEquals(2, foundProducts.size());
        assertEquals(product1.getProdId(), foundProducts.get(0).getProdId());
        verify(productRepository, times(1)).findAll();
    }
}