package com.example.apptracnghiem.util;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

    public void sendCongratToNewUser(Context context, String email,String username) {
        int timeout = 60000;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.timeout", timeout);

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("lytieulong2j2@gmail.com", "ngmm pgqt gknn ldbk");
            }
        });

        new Thread(() -> {
            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress("lytieulong2j2@gmail.com"));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                message.setSubject("Password Reset Request");
                String htmlContent = "<div style=\"font-family: Arial, sans-serif; line-height: 1.6;\">" +
                        "<h1 style=\"color: #333;\">Welcome to Our Service!</h1>" +
                        "<p style=\"font-size: 18px;\">We are thrilled to have you join our community. As a new member, " + username + " you now have access to exclusive features and benefits designed to enhance your experience.</p>" +
                        "<p style=\"font-size: 16px;\">If you have any questions or need assistance, our support team is here to help you. Don't hesitate to reach out.</p>" +
                        "<p style=\"font-size: 16px;\">Best regards,<br>The Team</p>" +
                        "</div>";

                message.setContent(htmlContent, "text/html");
                Transport.send(message);

            } catch (MessagingException e) {
                runOnUiThread(() -> Toast.makeText(context, "Error sending email", Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }
        }).start();
    }

    private void runOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
