package com.example.shoppingcart.service;

import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.repository.ProductRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    @Test
    public void testGetProductById() {
        ProductRepository productRepository = mock(ProductRepository.class);
        ProductService productService = new ProductServiceImpl(productRepository);

        Product product = new Product();
        product.setId("123");
        product.setName("Test Product");

        when(productRepository.findById("123")).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById("123");

        assertEquals("123", foundProduct.getId());
        assertEquals("Test Product", foundProduct.getName());
    }

    @Test
    public void testAddProduct() {
        ProductRepository productRepository = mock(ProductRepository.class);
        ProductService productService = new ProductServiceImpl(productRepository);

        Product product = new Product();
        product.setId("123");
        product.setName("Test Product");

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product addedProduct = productService.addProduct(product);

        assertEquals("123", addedProduct.getId());
        assertEquals("Test Product", addedProduct.getName());
    }

    @Test
    public void testUpdateProduct() {
        ProductRepository productRepository = mock(ProductRepository.class);
        ProductService productService = new ProductServiceImpl(productRepository);

        Product product = new Product();
        product.setId("123");
        product.setName("Test Product");

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = productService.updateProduct(product);

        assertEquals("123", updatedProduct.getId());
        assertEquals("Test Product", updatedProduct.getName());
    }

    @Test
    public void testDeleteProduct() {
        ProductRepository productRepository = mock(ProductRepository.class);
        ProductService productService = new ProductServiceImpl(productRepository);

        productService.deleteProduct("123");

        verify(productRepository, times(1)).deleteById("123");
    }
}
