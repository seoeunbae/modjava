package com.example.shoppingcart.service;

import com.example.shoppingcart.model.Cart;
import com.example.shoppingcart.model.CartItem;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.model.User;
import com.example.shoppingcart.repository.CartRepository;
import com.example.shoppingcart.repository.ProductRepository;
import com.example.shoppingcart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Cart getCartByUserEmail(String userEmail) {
        return cartRepository.findByUserEmail(userEmail);
    }

    @Override
    public Cart addItemToCart(String userEmail, String productId, int quantity) {
        Cart cart = getCartByUserEmail(userEmail);
        if (cart == null) {
            User user = userRepository.findByEmail(userEmail);
            cart = new Cart();
            cart.setUser(user);
            cart.setItems(new ArrayList<>());
        }

        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            boolean itemExists = false;
            for (CartItem item : cart.getItems()) {
                if (item.getProduct().getId().equals(productId)) {
                    item.setQuantity(item.getQuantity() + quantity);
                    itemExists = true;
                    break;
                }
            }
            if (!itemExists) {
                CartItem newItem = new CartItem();
                newItem.setProduct(product);
                newItem.setQuantity(quantity);
                newItem.setCart(cart);
                cart.getItems().add(newItem);
            }
        }
        return cartRepository.save(cart);
    }

    @Override
    public Cart updateItemInCart(String userEmail, String productId, int quantity) {
        Cart cart = getCartByUserEmail(userEmail);
        if (cart != null) {
            for (CartItem item : cart.getItems()) {
                if (item.getProduct().getId().equals(productId)) {
                    item.setQuantity(quantity);
                    break;
                }
            }
            return cartRepository.save(cart);
        }
        return null;
    }

    @Override
    public Cart removeItemFromCart(String userEmail, String productId) {
        Cart cart = getCartByUserEmail(userEmail);
        if (cart != null) {
            List<CartItem> items = cart.getItems();
            items.removeIf(item -> item.getProduct().getId().equals(productId));
            return cartRepository.save(cart);
        }
        return null;
    }
}
