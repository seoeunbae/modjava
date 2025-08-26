package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.example.shoppingcart.service.CartService;
import com.example.shoppingcart.repository.CartRepository;
import com.example.shoppingcart.service.UserService;
import com.example.shoppingcart.repository.UserRepository;
import com.example.shoppingcart.service.OrderService;
import com.example.shoppingcart.repository.OrderRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = ProductController.class, excludeAutoConfiguration = {JpaRepositoriesAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private CartService cartService;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product("P001", "Laptop", "Electronics", "Powerful laptop", 1200.00, 10, null);
        product2 = new Product("P002", "Mouse", "Electronics", "Wireless mouse", 25.00, 50, null);
    }

    //@Test
    //void addProduct_shouldReturnCreatedProduct() throws Exception {
    //    when(productService.addProduct(any(Product.class))).thenReturn(product1);
    //
    //    mockMvc.perform(post("/api/products")
    //                    .contentType(MediaType.APPLICATION_JSON)
    //                    .content(objectMapper.writeValueAsString(product1)))
    //            .andExpect(status().isCreated())
    //            .andExpect(jsonPath("$.prodId").value(product1.getProdId()));
    //
    //    verify(productService, times(1)).addProduct(any(Product.class));
    //}

    //@Test
    //void updateProduct_shouldReturnUpdatedProduct_whenProductExists() throws Exception {
    //    when(productService.updateProduct(any(Product.class))).thenReturn(product1);
    //
    //    mockMvc.perform(put("/api/products/{prodId}", product1.getProdId())
    //                    .contentType(MediaType.APPLICATION_JSON)
    //                    .content(objectMapper.writeValueAsString(product1)))
    //            .andExpect(status().isOk())
    //            .andExpect(jsonPath("$.prodId").value(product1.getProdId()));
    //
    //    verify(productService, times(1)).updateProduct(any(Product.class));
    //}

    //@Test
    //void updateProduct_shouldReturnBadRequest_whenPathVariableIdMismatch() throws Exception {
    //    mockMvc.perform(put("/api/products/{prodId}", "P999")
    //                    .contentType(MediaType.APPLICATION_JSON)
    //                    .content(objectMapper.writeValueAsString(product1)))
    //            .andExpect(status().isBadRequest());
    //
    //    verify(productService, never()).updateProduct(any(Product.class));
    //}

    //@Test
    //void updateProduct_shouldReturnNotFound_whenProductDoesNotExist() throws Exception {
    //    when(productService.updateProduct(any(Product.class))).thenThrow(new RuntimeException("Product not found"));
    //
    //    mockMvc.perform(put("/api/products/{prodId}", product1.getProdId())
    //                    .contentType(MediaType.APPLICATION_JSON)
    //                    .content(objectMapper.writeValueAsString(product1)))
    //            .andExpect(status().isNotFound());
    //
    //    verify(productService, times(1)).updateProduct(any(Product.class));
    //}

    //@Test
    //void deleteProduct_shouldReturnNoContent() throws Exception {
    //    doNothing().when(productService).deleteProduct(product1.getProdId());
    //
    //    mockMvc.perform(delete("/api/products/{prodId}", product1.getProdId()))
    //            .andExpect(status().isNoContent());
    //
    //    verify(productService, times(1)).deleteProduct(product1.getProdId());
    //}

    //@Test
    //void getProductById_shouldReturnProduct_whenProductExists() throws Exception {
    //    when(productService.getProductById(product1.getProdId())).thenReturn(Optional.of(product1));
    //
    //    mockMvc.perform(get("/api/products/{prodId}", product1.getProdId()))
    //            .andExpect(status().isOk())
    //            .andExpect(jsonPath("$.prodId").value(product1.getProdId()));
    //
    //    verify(productService, times(1)).getProductById(product1.getProdId());
    //}

    //@Test
    //void getProductById_shouldReturnNotFound_whenProductDoesNotExist() throws Exception {
    //    when(productService.getProductById(product1.getProdId())).thenReturn(Optional.empty());
    //
    //    mockMvc.perform(get("/api/products/{prodId}", product1.getProdId()))
    //            .andExpect(status().isNotFound());
    //
    //    verify(productService, times(1)).getProductById(product1.getProdId());
    //}

    //@Test
    //void getAllProducts_shouldReturnListOfProducts() throws Exception {
    //    when(productService.getAllProducts()).thenReturn(Arrays.asList(product1, product2));
    //
    //    mockMvc.perform(get("/api/products"))
    //            .andExpect(status().isOk())
    //            .andExpect(jsonPath("$[0].prodId").value(product1.getProdId()))
    //            .andExpect(jsonPath("$[1].prodId").value(product2.getProdId()));
    //
    //    verify(productService, times(1)).getAllProducts();
    //}
}