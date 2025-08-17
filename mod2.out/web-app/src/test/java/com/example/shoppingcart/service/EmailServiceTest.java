package com.example.shoppingcart.service;

import com.example.shoppingcart.model.Order;
import com.example.shoppingcart.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void sendRegistrationEmail() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");

        emailService.sendRegistrationEmail(user);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals("test@example.com", sentMessage.getTo()[0]);
        assertEquals("Welcome to Our Shopping Site!", sentMessage.getSubject());
        assertEquals("Dear Test User,\n\nThank you for registering with us!", sentMessage.getText());
    }

    @Test
    void sendOrderConfirmationEmail() {
        User user = new User();
        user.setEmail("customer@example.com");
        user.setName("Customer Name");

        Order order = new Order();
        order.setId(123L);
        order.setUser(user);

        emailService.sendOrderConfirmationEmail(order);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals("customer@example.com", sentMessage.getTo()[0]);
        assertEquals("Your Order Confirmation", sentMessage.getSubject());
        assertEquals("Dear Customer Name,\n\nYour order with ID 123 has been placed successfully.", sentMessage.getText());
    }

    @Test
    void sendShippingUpdateEmail() {
        User user = new User();
        user.setEmail("customer@example.com");
        user.setName("Customer Name");

        Order order = new Order();
        order.setId(456L);
        order.setUser(user);

        emailService.sendShippingUpdateEmail(order);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals("customer@example.com", sentMessage.getTo()[0]);
        assertEquals("Your Order Has Shipped!", sentMessage.getSubject());
        assertEquals("Dear Customer Name,\n\nYour order with ID 456 has been shipped.", sentMessage.getText());
    }
}
