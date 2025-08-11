package com.example.shoppingcart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.shoppingcart", "com.example.shoppingcart.dataaccess"})
@EntityScan(basePackages = {"com.example.shoppingcart.dataaccess.user", "com.example.shoppingcart.dataaccess.product", "com.example.shoppingcart.dataaccess.cart", "com.example.shoppingcart.dataaccess.order"})
@EnableJpaRepositories(basePackages = {"com.example.shoppingcart.dataaccess.user", "com.example.shoppingcart.dataaccess.product", "com.example.shoppingcart.dataaccess.cart", "com.example.shoppingcart.dataaccess.order"})
public class ShoppingCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartApplication.class, args);
    }

}
