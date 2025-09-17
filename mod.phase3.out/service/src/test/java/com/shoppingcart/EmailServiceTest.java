
package com.shoppingcart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendRegistrationSuccessEmail() throws MessagingException {
        // Given
        String to = "test@example.com";
        String name = "Test User";
        String htmlContent = "<html><body><h1>Welcome Test User</h1></body></html>";
        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(templateEngine.process(anyString(), any(Context.class))).thenReturn(htmlContent);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // When
        emailService.sendRegistrationSuccessEmail(to, name);

        // Then
        verify(templateEngine).process(anyString(), any(Context.class));
        verify(mailSender).send(mimeMessage);
    }

    @Test
    public void testSendTransactionSuccessEmail() throws MessagingException {
        // Given
        String to = "test@example.com";
        String name = "Test User";
        String transactionId = "12345";
        double amount = 100.0;
        String htmlContent = "<html><body><h1>Transaction Success</h1></body></html>";
        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(templateEngine.process(anyString(), any(Context.class))).thenReturn(htmlContent);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // When
        emailService.sendTransactionSuccessEmail(to, name, transactionId, amount);

        // Then
        verify(templateEngine).process(anyString(), any(Context.class));
        verify(mailSender).send(mimeMessage);
    }

    @Test
    public void testSendOrderShippedEmail() throws MessagingException {
        // Given
        String to = "test@example.com";
        String name = "Test User";
        String transactionId = "12345";
        double amount = 100.0;
        String htmlContent = "<html><body><h1>Order Shipped</h1></body></html>";
        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(templateEngine.process(anyString(), any(Context.class))).thenReturn(htmlContent);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // When
        emailService.sendOrderShippedEmail(to, name, transactionId, amount);

        // Then
        verify(templateEngine).process(anyString(), any(Context.class));
        verify(mailSender).send(mimeMessage);
    }

    @Test
    public void testSendProductAvailableEmail() throws MessagingException {
        // Given
        String to = "test@example.com";
        String name = "Test User";
        String productName = "Test Product";
        String productId = "123";
        String htmlContent = "<html><body><h1>Product Available</h1></body></html>";
        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(templateEngine.process(anyString(), any(Context.class))).thenReturn(htmlContent);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // When
        emailService.sendProductAvailableEmail(to, name, productName, productId);

        // Then
        verify(templateEngine).process(anyString(), any(Context.class));
        verify(mailSender).send(mimeMessage);
    }

    @Test
    public void testSendContactMessage() throws MessagingException {
        // Given
        String to = "test@example.com";
        String subject = "Test Subject";
        String messageContent = "Test Message";
        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // When
        emailService.sendContactMessage(to, subject, messageContent);

        // Then
        verify(mailSender).send(mimeMessage);
    }
}
