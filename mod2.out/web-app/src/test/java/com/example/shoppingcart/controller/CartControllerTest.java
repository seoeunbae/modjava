package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.Cart;
import com.example.shoppingcart.model.User;
import com.example.shoppingcart.service.CartService;
import com.example.shoppingcart.service.ProductService;
import com.example.shoppingcart.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private UserService userService;

    @MockBean
    private ProductService productService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        
        mockUser.setEmail("test@example.com");
        when(userService.findByEmail("test@example.com")).thenReturn(mockUser);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void viewCartDetails() throws Exception {
        when(cartService.getCartByUser("test@example.com")).thenReturn(new Cart(mockUser));

        mockMvc.perform(get("/user/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attributeExists("cart"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void addItemToCart() throws Exception {
        mockMvc.perform(post("/user/cart/add").param("productId", "prod1").param("quantity", "1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/cart"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void updateCartItemQuantity() throws Exception {
        mockMvc.perform(post("/user/cart/update").param("prodId", "1").param("quantity", "2").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/cart"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void removeCartItem() throws Exception {
        mockMvc.perform(post("/user/cart/remove").param("prodId", "1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/cart"));
    }
}