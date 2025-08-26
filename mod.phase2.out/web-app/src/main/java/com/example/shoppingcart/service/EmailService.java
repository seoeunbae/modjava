package com.example.shoppingcart.service;

import com.example.shoppingcart.model.Order;
import com.example.shoppingcart.model.Product;
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
        message.setText(String.join(System.lineSeparator(), 
                "Dear " + user.getName(), 
                "", 
                "Thank you for registering with us!"));
        // In a real application, you would send the email here.
        // For this migration, we will just log it.
        System.out.println("Sending registration email to: " + user.getEmail());
        mailSender.send(message);
    }

    public void sendOrderConfirmationEmail(Order order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(order.getUser().getEmail());
        message.setSubject("Your Order Confirmation");
        message.setText(String.join(System.lineSeparator(),
                "Dear " + order.getUser().getName(),
                "",
                "Your order with ID " + order.getId() + " has been placed successfully."));
        mailSender.send(message);
        System.out.println("Sending order confirmation email to: " + order.getUser().getEmail());
    }

    public void sendShippingUpdateEmail(Order order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(order.getUser().getEmail());
        message.setSubject("Your Order Has Shipped!");
        message.setText(String.join(System.lineSeparator(),
                "Dear " + order.getUser().getName(),
                "",
                "Your order with ID " + order.getId() + " has been shipped."));
        System.out.println("Sending shipping update email to: " + order.getUser().getEmail());
        mailSender.send(message);
    }

    public void sendProductAvailableEmail(User user, Product product) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Product Back in Stock!");
        message.setText(String.join(System.lineSeparator(),
                "Dear " + user.getName(),
                "",
                "The product you were waiting for, " + product.getProdName() + ", is now back in stock!"));
        System.out.println("Sending product available email to: " + user.getEmail());
        mailSender.send(message);
    }
}
