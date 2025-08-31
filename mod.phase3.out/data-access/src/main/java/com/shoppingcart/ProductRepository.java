
package com.shoppingcart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("SELECT new com.shoppingcart.Product(p.pid, p.image, p.info, p.name, p.price, p.quantity) FROM Product p")
    List<Product> findAllProducts();

    @Query("SELECT p FROM Product p WHERE lower(p.name) like lower(concat('%', :keyword, '%')) or lower(p.info) like lower(concat('%', :keyword, '%'))")
    List<Product> searchProducts(@Param("keyword") String keyword);

    

    
}
