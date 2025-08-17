package com.example.shoppingcart.service;

import com.example.shoppingcart.model.Order;
import com.example.shoppingcart.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendRegistrationEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Welcome to Our Shopping Site!");
        message.setText("Dear " + user.getName() + ",\n\nThank you for registering with us!");
        // In a real application, you would send the email here.
        // For this migration, we will just log it.
        System.out.println("Sending registration email to: " + user.getEmail());
        mailSender.send(message);
    }

    public void sendOrderConfirmationEmail(Order order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(order.getUser().getEmail());
        message.setSubject("Your Order Confirmation");
        message.setText("Dear " + order.getUser().getName() + ",\n\nYour order with ID " + order.getId() + " has been placed successfully.");
        mailSender.send(message);
        System.out.println("Sending order confirmation email to: " + order.getUser().getEmail());
    }

    public void sendShippingUpdateEmail(Order order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(order.getUser().getEmail());
        message.setSubject("Your Order Has Shipped!");
        message.setText("Dear " + order.getUser().getName() + ",\n\nYour order with ID " + order.getId() + " has been shipped.");
        System.out.println("Sending shipping update email to: " + order.getUser().getEmail());
        mailSender.send(message);
    }
}
