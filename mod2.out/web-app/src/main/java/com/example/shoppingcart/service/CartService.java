package com.example.shoppingcart.service;

import com.example.shoppingcart.model.Cart;

public interface CartService {

    Cart getCartByUserEmail(String userEmail);

    Cart addItemToCart(String userEmail, String productId, int quantity);

    Cart updateItemInCart(String userEmail, String productId, int quantity);

    Cart removeItemFromCart(String userEmail, String productId);
}
