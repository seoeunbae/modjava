package com.example.shoppingcart.service;

import com.example.shoppingcart.dataaccess.cart.Cart;
import com.example.shoppingcart.dataaccess.cart.CartItem;
import com.example.shoppingcart.dataaccess.cart.CartItemRepository;
import com.example.shoppingcart.dataaccess.cart.CartRepository;
import com.example.shoppingcart.dataaccess.product.Product;
import com.example.shoppingcart.dataaccess.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CartService cartService;

    private User testUser;
    private Product testProduct;
    private Cart testCart;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User("Test User", 1234567890L, "test@example.com", "123 Test St", 12345, "password");
        testUser.setId(1L);

        testProduct = new Product("P001", "Laptop", "Electronics", "Powerful laptop", 1200.00, 10, null);

        testCart = new Cart(testUser);
        testCart.setId(1L);
    }

    @Test
    void testAddItemToCart_newCart_newProduct() {
        when(userService.findByEmail(testUser.getEmail())).thenReturn(testUser);
        when(productService.getProductById(testProduct.getProdId())).thenReturn(Optional.of(testProduct));
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);
        when(cartItemRepository.findByCartAndProduct(testCart, testProduct)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart resultCart = cartService.addItemToCart(testUser.getEmail(), testProduct.getProdId(), 1);

        assertNotNull(resultCart);
        assertEquals(1, resultCart.getCartItems().size());
        verify(cartRepository, times(1)).save(any(Cart.class));
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testAddItemToCart_existingCart_newProduct() {
        when(userService.findByEmail(testUser.getEmail())).thenReturn(testUser);
        when(productService.getProductById(testProduct.getProdId())).thenReturn(Optional.of(testProduct));
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(cartItemRepository.findByCartAndProduct(testCart, testProduct)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart resultCart = cartService.addItemToCart(testUser.getEmail(), testProduct.getProdId(), 1);

        assertNotNull(resultCart);
        assertEquals(1, resultCart.getCartItems().size());
        verify(cartRepository, never()).save(any(Cart.class)); // Cart already exists
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testAddItemToCart_existingProductInCart() {
        CartItem existingCartItem = new CartItem(testCart, testProduct, 1);
        testCart.addCartItem(existingCartItem);

        when(userService.findByEmail(testUser.getEmail())).thenReturn(testUser);
        when(productService.getProductById(testProduct.getProdId())).thenReturn(Optional.of(testProduct));
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(cartItemRepository.findByCartAndProduct(testCart, testProduct)).thenReturn(Optional.of(existingCartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart resultCart = cartService.addItemToCart(testUser.getEmail(), testProduct.getProdId(), 2);

        assertNotNull(resultCart);
        assertEquals(1, resultCart.getCartItems().size());
        assertEquals(3, existingCartItem.getQuantity()); // 1 (initial) + 2 (added) = 3
        verify(cartItemRepository, times(1)).save(existingCartItem);
    }

    @Test
    void testUpdateCartItemQuantity_increase() {
        CartItem existingCartItem = new CartItem(testCart, testProduct, 1);
        testCart.addCartItem(existingCartItem);

        when(userService.findByEmail(testUser.getEmail())).thenReturn(testUser);
        when(productService.getProductById(testProduct.getProdId())).thenReturn(Optional.of(testProduct));
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(cartItemRepository.findByCartAndProduct(testCart, testProduct)).thenReturn(Optional.of(existingCartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart resultCart = cartService.updateCartItemQuantity(testUser.getEmail(), testProduct.getProdId(), 5);

        assertNotNull(resultCart);
        assertEquals(5, existingCartItem.getQuantity());
        verify(cartItemRepository, times(1)).save(existingCartItem);
    }

    @Test
    void testUpdateCartItemQuantity_removeByZero() {
        CartItem existingCartItem = new CartItem(testCart, testProduct, 1);
        testCart.addCartItem(existingCartItem);

        when(userService.findByEmail(testUser.getEmail())).thenReturn(testUser);
        when(productService.getProductById(testProduct.getProdId())).thenReturn(Optional.of(testProduct));
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(cartItemRepository.findByCartAndProduct(testCart, testProduct)).thenReturn(Optional.of(existingCartItem));
        doNothing().when(cartItemRepository).delete(existingCartItem);

        Cart resultCart = cartService.updateCartItemQuantity(testUser.getEmail(), testProduct.getProdId(), 0);

        assertNotNull(resultCart);
        assertTrue(resultCart.getCartItems().isEmpty());
        verify(cartItemRepository, times(1)).delete(existingCartItem);
    }

    @Test
    void testRemoveCartItem() {
        CartItem existingCartItem = new CartItem(testCart, testProduct, 1);
        testCart.addCartItem(existingCartItem);

        when(userService.findByEmail(testUser.getEmail())).thenReturn(testUser);
        when(productService.getProductById(testProduct.getProdId())).thenReturn(Optional.of(testProduct));
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        when(cartItemRepository.findByCartAndProduct(testCart, testProduct)).thenReturn(Optional.of(existingCartItem));
        doNothing().when(cartItemRepository).delete(existingCartItem);

        cartService.removeCartItem(testUser.getEmail(), testProduct.getProdId());

        assertTrue(testCart.getCartItems().isEmpty());
        verify(cartItemRepository, times(1)).delete(existingCartItem);
    }

    @Test
    void testGetCartByUserFound() {
        when(userService.findByEmail(testUser.getEmail())).thenReturn(testUser);
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));

        Cart resultCart = cartService.getCartByUser(testUser.getEmail());

        assertNotNull(resultCart);
        assertEquals(testCart.getId(), resultCart.getId());
    }

    @Test
    void testGetCartByUserNotFound() {
        when(userService.findByEmail(testUser.getEmail())).thenReturn(testUser);
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.empty());

        Cart resultCart = cartService.getCartByUser(testUser.getEmail());

        assertNull(resultCart);
    }

    @Test
    void testClearCart() {
        CartItem item1 = new CartItem(testCart, testProduct, 1);
        CartItem item2 = new CartItem(testCart, new Product("P002", "Keyboard", "Electronics", "", 50.0, 5, null), 2);
        testCart.addCartItem(item1);
        testCart.addCartItem(item2);

        when(userService.findByEmail(testUser.getEmail())).thenReturn(testUser);
        when(cartRepository.findByUser(testUser)).thenReturn(Optional.of(testCart));
        doNothing().when(cartItemRepository).deleteAll(anySet());
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        cartService.clearCart(testUser.getEmail());

        assertTrue(testCart.getCartItems().isEmpty());
        verify(cartItemRepository, times(1)).deleteAll(anySet());
        verify(cartRepository, times(1)).save(testCart);
    }
}
