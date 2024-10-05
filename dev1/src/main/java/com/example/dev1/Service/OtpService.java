package com.example.dev1.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {
    @Autowired
    private JavaMailSender javaMailSender;
    // Store OTPs mapped to employee IDs
    private Map<String, Integer> otpStore = new HashMap<>();

    public String generateOtp(int employeeId) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(otp, employeeId);  // Store OTP with employee ID
        return otp;
    }

    public void sendOtpEmail(String toEmail, String otp) throws MessagingException {
        if (!isValidEmail(toEmail)) {
            throw new MessagingException("Invalid email address: " + toEmail);
        }

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(toEmail);
        helper.setSubject("Your OTP Code");
        helper.setText("Your OTP code is: " + otp);
        javaMailSender.send(message);
    }
    public void sendEmail12(String toEmail, String subject, String message) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(message, false);

        // Send the email
        javaMailSender.send(mimeMessage);
    }
    public boolean verifyOtp(String otp) {
        return otpStore.containsKey(otp);
    }

    private boolean isValidEmail(String email) {
        return email != null && !email.isEmpty() && email.contains("@");
    }
}
