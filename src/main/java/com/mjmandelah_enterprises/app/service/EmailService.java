package com.mjmandelah_enterprises.app.service;

import com.mjmandelah_enterprises.app.exception.EmailSendException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            emailSender.send(message);
        } catch (MailException e) {
            // Log the error for further inspection
            e.printStackTrace();
            // Throw a custom exception with a meaningful message
            throw new EmailSendException("Failed to send email: " + e.getMessage());
        }
    }
}
