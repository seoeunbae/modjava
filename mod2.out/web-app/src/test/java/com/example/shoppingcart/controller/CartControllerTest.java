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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private CartController cartController;

    private User mockUser;
    private Principal mockPrincipal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");

        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(mockUser.getEmail())
                .password("password")
                .roles("USER")
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        mockPrincipal = new Principal() {
            @Override
            public String getName() {
                return mockUser.getEmail();
            }
        };

        when(userService.findByEmail(mockUser.getEmail())).thenReturn(mockUser);
    }

    @Test
    void viewCartDetails() throws Exception {
        Cart cart = new Cart();
        cart.setUser(mockUser);
        cart.setCartItems(new HashSet<>(Arrays.asList(new CartItem(), new CartItem())));

        when(cartService.getCartByUser(mockUser.getEmail())).thenReturn(cart);

        mockMvc.perform(get("/cart").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(view().name("cartDetails"))
                .andExpect(model().attributeExists("cart"));

        verify(cartService, times(1)).getCartByUser(mockUser.getEmail());
    }

    @Test
    void addToCart() throws Exception {
        Product product = new Product();
        product.setProdId("prod1");
        product.setProdPrice(100.0);

        when(cartService.addItemToCart(anyString(), anyString(), anyInt())).thenReturn(new Cart());

        mockMvc.perform(post("/cart/add").principal(mockPrincipal)
                        .param("prodId", "prod1")
                        .param("quantity", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cartService, times(1)).addItemToCart(eq(mockUser.getEmail()), eq("prod1"), eq(1));
    }

    @Test
    void updateCart() throws Exception {
        Cart cart = new Cart();
        cart.setUser(mockUser);
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCart(cart);
        cart.setCartItems(new HashSet<>(Arrays.asList(cartItem)));

        when(cartService.updateCartItemQuantity(anyString(), anyString(), anyInt())).thenReturn(cart);

        mockMvc.perform(post("/cart/update").principal(mockPrincipal)
                        .param("prodId", "1")
                        .param("quantity", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cartService, times(1)).updateCartItemQuantity(eq(mockUser.getEmail()), eq("1"), eq(2));
    }

    @Test
    void removeProductFromCart() throws Exception {
        doNothing().when(cartService).removeCartItem(anyString(), anyString());

        mockMvc.perform(post("/cart/remove").principal(mockPrincipal)
                        .param("prodId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cartService, times(1)).removeCartItem(eq(mockUser.getEmail()), eq("1"));
    }

    @Test
    void clearCart() throws Exception {
        doNothing().when(cartService).clearCart(anyString());

        mockMvc.perform(post("/cart/clear").principal(mockPrincipal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cartService, times(1)).clearCart(eq(mockUser.getEmail()));
    }
}
