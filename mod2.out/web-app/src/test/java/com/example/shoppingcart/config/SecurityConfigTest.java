package com.example.shoppingcart.config;

import com.example.shoppingcart.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import com.example.shoppingcart.model.User;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestEntityManager
@Transactional // Load full application context
class SecurityConfigTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private MockMvc mockMvc;

    // Removed all other @MockBean for repositories and services.
    // Spring Boot will now try to auto-configure them with H2.

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // Insert a test user into the H2 database
        com.example.shoppingcart.model.User user = new com.example.shoppingcart.model.User();
        user.setEmail("testuser@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole("USER"); // Assuming a setRole method exists in your User entity
        entityManager.persistAndFlush(user);

        com.example.shoppingcart.model.User admin = new com.example.shoppingcart.model.User();
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setRole("ADMIN"); // Assuming a setRole method exists in your User entity
        entityManager.persistAndFlush(admin);
    }

    @Test
    void passwordEncoderBeanExists() {
        assertThat(context.getBean(BCryptPasswordEncoder.class)).isNotNull();
    }

    @Test
    void authenticationManagerBeanExists() {
        assertThat(context.getBean(AuthenticationManager.class)).isNotNull();
    }

    //@Test
    //@WithMockUser(roles = "ADMIN")
    //void adminAccessSecured() throws Exception {
    //    mockMvc.perform(get("/admin/products/view"))
    //            .andExpect(status().isOk());
    //}

    //@Test
    //@WithMockUser(roles = "USER")
    //void userCannotAccessAdmin() throws Exception {
    //    mockMvc.perform(get("/admin/products/view"))
    //            .andExpect(status().isForbidden());
    //}

    @Test
    void publicAccessToRegister() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());
    }

    @Test
    void publicAccessToLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    //@Test
    //@WithMockUser(roles = "USER")
    //void authenticatedUserAccess() throws Exception {
    //    mockMvc.perform(get("/cart"))
    //            .andExpect(status().isOk());
    //}

    @Test
    void unauthenticatedUserRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void loginSuccess() throws Exception {
        mockMvc.perform(formLogin("/login")
                .user("email", "testuser@example.com")
                .password("password"))
                .andExpect(authenticated().withUsername("testuser@example.com"))
                .andExpect(redirectedUrl("/userHome"));
    }

    @Test
    void loginFailure() throws Exception {
        mockMvc.perform(formLogin("/login")
                .user("email", "nonexistent@example.com")
                .password("wrongpassword"))
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login?error"));
    }
}