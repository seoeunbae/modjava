package com.example.shoppingcart;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShoppingCartApplicationTests {

    @Test
    void contextLoads() {
        // Verifies that the application context loads without errors
    }

    @Test
    void applicationMain() {
        ShoppingCartApplication.main(new String[]{});
    }

}
