package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.Cart;
import com.example.shoppingcart.model.CartItem;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.model.User;
import com.example.shoppingcart.service.CartService;
import com.example.shoppingcart.service.ProductService;
import com.example.shoppingcart.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

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
    private ProductService productService;

    @MockBean
    private UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void viewCartDetails() throws Exception {
        Cart cart = new Cart();
        cart.setUser(mockUser);
        cart.setCartItems(new HashSet<>(Arrays.asList(new CartItem(), new CartItem())));

        when(cartService.getCartByUser(mockUser.getEmail())).thenReturn(cart);

        mockMvc.perform(get("/api/cart").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email").value(mockUser.getEmail()))
                .andExpect(jsonPath("$.cartItems").isArray());

        verify(cartService, times(1)).getCartByUser(mockUser.getEmail());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void addToCart() throws Exception {
        Product product = new Product();
        product.setProdId("prod1");
        product.setProdPrice(100.0);

        when(cartService.addItemToCart(anyString(), anyString(), anyInt())).thenReturn(new Cart());

        mockMvc.perform(post("/api/cart/add").param("prodId", "prod1").param("quantity", "1").with(csrf()))
                .andExpect(status().isOk());

        verify(cartService, times(1)).addItemToCart(eq(mockUser.getEmail()), eq("prod1"), eq(1));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void updateCart() throws Exception {
        Cart cart = new Cart();
        cart.setUser(mockUser);
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCart(cart);
        cart.setCartItems(new HashSet<>(Arrays.asList(cartItem)));

        when(cartService.updateCartItemQuantity(anyString(), anyString(), anyInt())).thenReturn(new Cart());

        mockMvc.perform(post("/api/cart/update").param("prodId", "1").param("quantity", "2").with(csrf()))
                .andExpect(status().isOk());

        verify(cartService, times(1)).updateCartItemQuantity(eq(mockUser.getEmail()), eq("1"), eq(2));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void removeProductFromCart() throws Exception {
        doNothing().when(cartService).removeCartItem(anyString(), anyString());

        mockMvc.perform(post("/api/cart/remove").param("prodId", "1").with(csrf()))
                .andExpect(status().isOk());

        verify(cartService, times(1)).removeCartItem(eq(mockUser.getEmail()), eq("1"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void clearCart() throws Exception {
        doNothing().when(cartService).clearCart(anyString());

        mockMvc.perform(post("/api/cart/clear").with(csrf()))
                .andExpect(status().isOk());

        verify(cartService, times(1)).clearCart(eq(mockUser.getEmail()));
    }
}