package com.example.shoppingcart.integration;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import java.util.Map;

public class TestcontainersInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    static {
        postgreSQLContainer.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        MapPropertySource testcontainers = new MapPropertySource("testcontainers", Map.of(
                "spring.datasource.url", postgreSQLContainer.getJdbcUrl(),
                "spring.datasource.username", postgreSQLContainer.getUsername(),
                "spring.datasource.password", postgreSQLContainer.getPassword()
        ));
        applicationContext.getEnvironment().getPropertySources().addFirst(testcontainers);
    }
}
