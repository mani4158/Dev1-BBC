package com.example.dev1.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP Code is: " + otp);
        mailSender.send(message);
    }
    public void sendInvoiceEmail(String customerEmail, ByteArrayOutputStream invoicePdf) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(customerEmail);
        helper.setSubject("Your Invoice");
        helper.setText("Dear Customer,\n\nPlease find attached your invoice.\n\nRegards,\nYour Company");

        // Add PDF as an attachment
        helper.addAttachment("invoice.pdf", new ByteArrayResource(invoicePdf.toByteArray()));

        mailSender.send(message);
    }
}
