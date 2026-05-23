package org.example.quiet_place.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;


    @Async
    public void sendWelcomeEmail(String toEmail, String username) {

        System.out.println("1- Entered sendWelcomeEmail method");
        System.out.println("2- Sending email to: " + toEmail);


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Welcome to QuietPlace Finder! 🎧");
        message.setText(
                "Hello " + username + ",\n\n" +
                        "Welcome to QuietPlace Finder! We're excited to have you on board.\n\n" +
                        "With our app, you can:\n" +
                        "✓ Discover quiet places near you\n" +
                        "✓ Read and write reviews about places\n" +
                        "✓ Save your favorite spots\n" +
                        "\n\nBest regards,\n" +
                        "The QuietPlace Team"
        );

        mailSender.send(message);
        System.out.println("Welcome email sent to: " + toEmail);
    }
}