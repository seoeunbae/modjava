package com.shoppingcart;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void whenSearchByNameOrInfo_thenReturnMatchingProducts() {
        // given
        Product product1 = new Product("1", "image1.jpg", "A great book about Java", "Java Programming", 10.0, 10);
        entityManager.persist(product1);
        Product product2 = new Product("2", "image2.jpg", "A great book about Python", "Python Programming", 20.0, 5);
        entityManager.persist(product2);
        Product product3 = new Product("3", "image3.jpg", "A useful guide to SQL", "SQL Guide", 15.0, 12);
        entityManager.persist(product3);
        entityManager.flush();

        // when
        List<Product> foundProducts = productRepository.searchProducts("book");

        // then
        assertThat(foundProducts).hasSize(2);
        assertThat(foundProducts).extracting(Product::getName).contains("Java Programming", "Python Programming");
    }

    @Test
    public void whenSearchByName_thenReturnMatchingProducts() {
        // given
        Product product1 = new Product("1", "image1.jpg", "A great book about Java", "Java Programming", 10.0, 10);
        entityManager.persist(product1);
        Product product2 = new Product("2", "image2.jpg", "A great book about Python", "Python Programming", 20.0, 5);
        entityManager.persist(product2);
        Product product3 = new Product("3", "image3.jpg", "A useful guide to SQL", "SQL Guide", 15.0, 12);
        entityManager.persist(product3);
        entityManager.flush();

        // when
        List<Product> foundProducts = productRepository.searchProducts("java");

        // then
        assertThat(foundProducts).hasSize(1);
        assertThat(foundProducts.get(0).getName()).isEqualTo("Java Programming");
    }

    @Test
    public void whenSearchByInfo_thenReturnMatchingProducts() {
        // given
        Product product1 = new Product("1", "image1.jpg", "A great book about Java", "Java Programming", 10.0, 10);
        entityManager.persist(product1);
        Product product2 = new Product("2", "image2.jpg", "A great book about Python", "Python Programming", 20.0, 5);
        entityManager.persist(product2);
        Product product3 = new Product("3", "image3.jpg", "A useful guide to SQL", "SQL Guide", 15.0, 12);
        entityManager.persist(product3);
        entityManager.flush();

        // when
        List<Product> foundProducts = productRepository.searchProducts("sql");

        // then
        assertThat(foundProducts).hasSize(1);
        assertThat(foundProducts.get(0).getName()).isEqualTo("SQL Guide");
    }
}