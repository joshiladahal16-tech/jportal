package com.job.jportal.service;

import com.job.jportal.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    public void sendWelcomeEmail(User user) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(user.getEmail());
            message.setSubject("Welcome to Job Portal!");
            message.setText(buildWelcomeMessage(user));
            
            mailSender.send(message);
            System.out.println("Welcome email sent to: " + user.getEmail());
        } catch (Exception e) {
            System.err.println("Failed to send welcome email to: " + user.getEmail());
            System.err.println("Error: " + e.getMessage());
        }
    }

    private String buildWelcomeMessage(User user) {
        return String.format("""
            Hello %s %s,
            
            Welcome to Job Portal! We're excited to have you join our community.
            
            Your account has been successfully created with the following details:
            • Email: %s
            • Role: %s
            • Registration Date: %s
            
            You can now:
            • Browse and search for jobs
            • Apply to positions that interest you
            • Track your applications
            • Update your profile
            
            If you have any questions, feel free to contact our support team.
            
            Best regards,
            The Job Portal Team
            """, 
            user.getFirstName(), 
            user.getLastName(), 
            user.getEmail(), 
            user.getRole(), 
            java.time.LocalDateTime.now().toString()
        );
    }
}
